package jss.proto.define;

/**
 * include/mysql_com.h
 */
public interface ComDefine
{

    /**
     * Length of random string sent by server on handshake; this is also length of
     * obfuscated password, recieved from client
     */
    public static final int SCRAMBLE_LENGTH = 20;

    ;
    public static final int SCRAMBLE_LENGTH_323 = 8;
    /**
     * length of password stored in the db: new passwords are preceeded with '*'
     */
    public static final int SCRAMBLED_PASSWORD_CHAR_LENGTH = (SCRAMBLE_LENGTH * 2 + 1);
    public static final int SCRAMBLED_PASSWORD_CHAR_LENGTH_323 = (SCRAMBLE_LENGTH_323 * 2);

}
