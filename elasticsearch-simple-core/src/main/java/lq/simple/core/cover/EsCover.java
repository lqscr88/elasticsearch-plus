package lq.simple.core.cover;


/**
 * es转换服务
 *
 * @author lqscr88
 * @date 2023/08/30
 */
public  interface EsCover{

    /**
     * 转换
     *
     * @param response 响应
     * @return {@link T}
     */
    <T> T cover(String response);

}
