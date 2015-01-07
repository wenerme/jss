package jss.proto.define;

public enum ConnectionPhase
{
    READ_AUTH, SEND_AUTH, READ_AUTH_RESULT, SEND_AUTH_RESULT,

    READ_COMMAND, SEND_COMMAND, READ_COMMAND_RESULT, SEND_COMMAND_RESULT,

    /**
     * 初始化
     */
    INITIALIZE,
    /**
     * 认证
     */
    AUTHENTICATE,
    /**
     * 链接建立完成,处理命令
     */
    COMMAND_PHASE,
    /**
     * 正在断开链接
     */
    DISCONNECTING,
    /**
     * 链接已断开
     */
    DISCONNECTED
}
