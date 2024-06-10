package Decor;

import Decor.IComponent;

import java.awt.Graphics;

public abstract class Decorator implements IComponent {
    protected IComponent component;

    public Decorator(IComponent component) {
        this.component = component;
    }

    public void drawBoard(Graphics g, int sW, int sH) {
        component.drawBoard(g, sW, sH);
    }

    @Override
    public void loadImage() {
        component.loadImage();
    }
}
