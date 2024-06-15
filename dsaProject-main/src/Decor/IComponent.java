/*
Name: Trinh Binh Nguyen
Purpose: Defines the IComponent interface for implementing components that can draw a board and load images.
*/

package Decor;

import java.awt.Graphics;

public interface IComponent {
    void drawBoard(Graphics g, int sW, int sH);
    void loadImage();
}
