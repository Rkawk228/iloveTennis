package com.hoyong.ilote.core.handler;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoyong.ilote.core.response.ResponseBase;
import com.hoyong.ilote.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RestControllerAdvice
public class LovTeExceptionHandler {

    /**
     *
     * 업무명 : @Valid Exception 처리
     * 최초작성일 : 2021. 7. 28.
     * @param me
     * @param request
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseBase<Object> handleIllegalArgumentException(MethodArgumentNotValidException e) {
        log.info("MethodArgumentNotValidException.class {} ",e);
        String errCd = "000004";
        String timestamp = getTimestamp();

        return ResponseBase.error(HttpStatus.NOT_FOUND.value(),errCd,makeValidationMsg(e.getBindingResult()),timestamp);
    }

    /**
     * 404 오류 처리(AOP를 타지 않음)
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseBase<Object> handleNotFoundError(NoHandlerFoundException e, HttpServletRequest request){
        log.info ("NoHandlerFoundException.class {} ",e);
        String errCd = "000002";
        String timestamp = ExceptionLogPrint(e,request,errCd);
        return ResponseBase.error(HttpStatus.NOT_FOUND.value(),errCd,e.getMessage(),timestamp);
    }

    /**
     *
     * 업무명 : NullPointException 처리
     * 최초작성일 : 2021. 7. 19.
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseBase<Object> NullPointerException(Exception e, HttpServletRequest request){
        log.info ("NullPointerException.class {} ",e);
        String errCd = "000001";
        String message = "java.lang.NullPointerException: null";
        String timestamp = getTimestamp();
        return ResponseBase.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),errCd,message,timestamp);
    }

    /**
     * BusinessException 오류 처리
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseBase<Object> handleBusinessException(final BusinessException e,HttpServletRequest request){
        log.info ("BusinessException.class {} ",e);
        String errCd = e.getErrCd();
        String message = e.getMsg();
        String timestamp = getTimestamp();

        return ResponseBase.error(HttpStatus.BAD_REQUEST.value(),errCd,message,timestamp);
    }

    /**
     * 공통 오류 처리
     * @param e
     * @param request
     * @return
     */

    @ExceptionHandler(Exception.class)
    public ResponseBase<Object> handleException(Exception e, HttpServletRequest request){
        log.info ("Exception.class {} ",e);
        String errCd = "000001";
        String timestamp = getTimestamp();
        return ResponseBase.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),errCd,e.getMessage(),timestamp);
    }


    /**
     * 공통 로그 출력
     * @param e
     * @param request
     * @return
     */
    private static String ExceptionLogPrint(Exception e,HttpServletRequest request,String errCd){
        String timestamp = getTimestamp();
        Map<Object, Object> paramMap = null;
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("ERR_CD",errCd);
        errorMap.put("ERR_MSG",e.toString());
        errorMap.put("Request_Session",getSession(request));
        errorMap.put("Request_Header",getHeader(request));

        if(request.getHeader("content-type") == null || request.getHeader("content-type").indexOf("json") == -1 ){
            paramMap = getBodyParam(request);
        } else if(request.getHeader("content-type").indexOf("json") > -1){  //json 요청
            paramMap = getBody(request);
        }

        errorMap.put("Request_Param",paramMap);
        errorMap.put("Request_URI",request.getRequestURI());
        errorMap.put("Request_HttpMethod",request.getMethod());
        errorMap.put("Request_ServletPath",request.getServletPath());
        errorMap.put("timestamp",timestamp);

        ObjectMapper mapper = new ObjectMapper();
        try{
            log.error("ERR : {} / {}",mapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorMap),e);
        }catch(JsonProcessingException je){
            je.printStackTrace();
        }

        return timestamp;
    }

    /**
     * timestamp 가져오기
     * @param
     * @param
     * @return
     */
    private static String getTimestamp(){
        final String pattern = "yyyy-MM-dd HH:mm:ss.SSS";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String timestamp = simpleDateFormat.format(new Date());

        return timestamp;
    }

    /**
     * Get Session param
     * @param request
     * @return
     */
    private static Map<Object, Object> getSession(HttpServletRequest request){
        Map<Object, Object> sessionMap = new HashMap<>();
        Enumeration<String> sessionEnum = request.getSession().getAttributeNames();
        while(sessionEnum.hasMoreElements()){
            String sessionAttribute = sessionEnum.nextElement();
            sessionMap.put(sessionAttribute,request.getSession().getAttribute(sessionAttribute));
        }

        return sessionMap;
    }

    /**
     * Get Header param
     * @param request
     * @return
     */
    private static Map<Object, Object> getHeader(HttpServletRequest request){
        Map<Object, Object> headerMap = new HashMap<>();
        Enumeration headerEnum = request.getHeaderNames();
        while(headerEnum.hasMoreElements()){
            String headerName = (String)headerEnum.nextElement();
            String headerValue = request.getHeader(headerName);
            headerMap.put(headerName,headerValue);
        }

        return headerMap;
    }


    /**
     * request param type
     * @param request
     * @return
     */
    private static Map<Object, Object> getBodyParam(HttpServletRequest request){
        Map<Object, Object> _param = new HashMap<>();
        request.getParameterMap().forEach((key, value)->{
            if(value != null && value.length > 1) {
                _param.put(key, value);
            } else {
                _param.put(key, value[0]);
            }
        });
        return _param;
    }

    /**
     * request body(json) type
     * @param request
     * @return
     */
    private static Map<Object, Object> getBody(HttpServletRequest request) {
        String body;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = request.getInputStream();  //스트림에서 데이터를 전부 읽습니다.
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (bufferedReader != null) { try { bufferedReader.close(); } catch (IOException ex) { ex.printStackTrace(); } }
        }
        body = stringBuilder.toString();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TypeReference<Map<Object, Object>> typeReference = new TypeReference<Map<Object,Object>>() {};

            return objectMapper.readValue(body, typeReference);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }


    /**
     *
     * 업무명 : Args 메세지 처리
     * 최초작성일 : 2021. 5. 13.
     * @param message
     * @param msgArgs
     * @return
     */
    private static String getArrMessage(String message,String[] msgArgs) {
        String msg =  message;
        for(int i=0;i<msgArgs.length;i++) {
            String replaceStr = "{" + i + "}";
            msg = StringUtils.replace(msg, replaceStr, msgArgs[i]);
        }

        return msg;
    }

    /**
     *
     * 업무명 : @Valid 메세지 처리
     * 최초작성일 : 2021. 7. 28.
     * @param bindingResult
     * @return
     */
    private static String makeValidationMsg(BindingResult bindingResult){
        String str = "";

        // 에러가 있는지 확인하여 처리
        if(bindingResult.hasErrors()) {
            StringBuffer sb = new StringBuffer();

            // 에러 메세지 만들기
            for(FieldError error : bindingResult.getFieldErrors()) {
                String msg = error.getField() + " : " + error.getDefaultMessage() + ",  ";
                sb = sb.append(msg);
            }

            str = sb.toString();
        }

        return str;
    }
}
