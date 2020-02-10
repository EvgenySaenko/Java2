package chat_common;

public class Library {

    /*
     * auth_request login password    запрос авторизации
     * auth_accept nickname           подтверждение авторизации
     * auth_error                     ошибка авторизации
     * broadcast msg                   сообщения которые будут посылаться всем
     * msg_format_error msg
     * */
    public static final String DELIMITER = "±";
    public static final String AUTH_REQUEST = "/auth_request"; //запрос авторизации
    public static final String AUTH_ACCEPT = "/auth_accept";   // подтверждение авторизации
    public static final String AUTH_DENIED = "/auth_denied";  //отказ в авторизации
    public static final String MSG_FORMAT_ERROR = "/msg_format_error";
    //если мы вдруг не поняли, что за сообщение и не смогли разобрать
    public static final String TYPE_BROADCAST = "/bcast";
    //то есть сообщение которое будет посылаться всем


    //сокет авторизовался
    public static String getAuthRequest(String login, String password){
        return AUTH_REQUEST + DELIMITER + login + DELIMITER + password;
    }
    //сокет получил подтверждение авторизации
    public static String getAuthAccept(String nickname){
        return AUTH_ACCEPT + DELIMITER + nickname;
    }

    public static String getAuthDenied(){
        return AUTH_DENIED;
    }

    public static String getMsgFormatError(String message){
        return  MSG_FORMAT_ERROR + DELIMITER + message;
    }

    public static String getTypeBroadcast(String src, String message){
        return TYPE_BROADCAST + DELIMITER + System.currentTimeMillis() + DELIMITER + src + DELIMITER + message;
    }


}