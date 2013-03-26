package casmi.timeline;

import casmi.graphics.Graphics;

abstract public class MyDissolve extends Dissolve{

    public MyDissolve(double time) {
        super(DissolveMode.ORIGINAL, time);
    }

    @Override
    protected void startMyDissolve(Scene nowScene, Scene nextScene){
        startDissolve(nowScene, nextScene);
    }

    @Override
    protected void setMyDissolve(Scene nowScene, Scene nextScene, double nowDissolveRate, Graphics g){
        nowScene.setSceneA(1.0, g);
        nextScene.setSceneA(1.0, g);
        dissolveRender(nowScene, nextScene, nowDissolveRate);
        nowScene.setDepthTest(false);
        if(nowDissolve)
            nowScene.drawscene(g);
        nextScene.drawscene(g);
    }

    @Override
    protected void endMyDissolve(Scene nowScene, Scene nextScene) {
        endDissolve(nowScene, nextScene);
    }

    abstract public void endDissolve(Scene nowScene, Scene nextScene);

    abstract public void dissolveRender(Scene nowScene, Scene nextScene, double nowDissolveRate);

    abstract public void startDissolve(Scene nowScene, Scene nextScene);
}
