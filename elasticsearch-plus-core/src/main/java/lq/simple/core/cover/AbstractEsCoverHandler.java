package lq.simple.core.cover;



public abstract class AbstractEsCoverHandler implements EsCover {

    @Override
    public <T> T cover(String response) {
        return doCover(response);
    }

    protected abstract  <T> T doCover(String response);


}
