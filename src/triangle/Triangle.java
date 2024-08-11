package triangle;

import resizable.ResizableImage;

import java.awt.*;
import java.awt.image.BufferedImage;

import static resizable.Debug.print;

public class Triangle implements ResizableImage {
    int drawTriangle = 0;

    private BufferedImage drawTriangle(Dimension size) {
        print("drawTriangle: " + ++drawTriangle + " size: " + size);
        BufferedImage bufferedImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gBuffer = (Graphics2D) bufferedImage.getGraphics();

        int[] xPoints = {size.width / 2, 0, size.width};
        int[] yPoints = {0, size.height, size.height};

        int maxLevel = 5; // 5 уровней для точного соответствия порядку цветов
        drawSierpinski(gBuffer, xPoints, yPoints, maxLevel, maxLevel);

        return bufferedImage;
    }

    private void drawSierpinski(Graphics g, int[] xPoints, int[] yPoints, int currentLevel, int maxLevel) {
        if (currentLevel == 0) {
            g.setColor(getColorForLevel(currentLevel, maxLevel));
            g.fillPolygon(xPoints, yPoints, 3);

            // Добавляем черную обводку
            g.setColor(Color.BLACK);
            g.drawPolygon(xPoints, yPoints, 3);
            return;
        }

        int[] midX = {(xPoints[0] + xPoints[1]) / 2, (xPoints[1] + xPoints[2]) / 2, (xPoints[2] + xPoints[0]) / 2};
        int[] midY = {(yPoints[0] + yPoints[1]) / 2, (yPoints[1] + yPoints[2]) / 2, (yPoints[2] + yPoints[0]) / 2};

        g.setColor(getColorForLevel(currentLevel, maxLevel));
        g.fillPolygon(midX, midY, 3);

        // Добавляем черную обводку
        g.setColor(Color.BLACK);
        g.drawPolygon(midX, midY, 3);

        drawSierpinski(g, new int[]{xPoints[0], midX[0], midX[2]}, new int[]{yPoints[0], midY[0], midY[2]}, currentLevel - 1, maxLevel);
        drawSierpinski(g, new int[]{midX[0], xPoints[1], midX[1]}, new int[]{midY[0], yPoints[1], midY[1]}, currentLevel - 1, maxLevel);
        drawSierpinski(g, new int[]{midX[2], midX[1], xPoints[2]}, new int[]{midY[2], midY[1], yPoints[2]}, currentLevel - 1, maxLevel);
    }

    private Color getColorForLevel(int level, int maxLevel) {
        Color[] colors = {
                new Color(255, 182, 193),  // Light Pink (самый внутренний уровень)
                new Color(255, 223, 186),  // Light Yellow
                new Color(255, 255, 153),  // Light Blue
                new Color(187, 255, 255),
                new Color(187, 255, 255),
                new Color(187, 255, 255)// Lavender (внешний уровень)

        };
        return colors[(maxLevel - level) % colors.length];
    }

    BufferedImage bufferedImage;
    Dimension bufferedImageSize;

    @Override
    public Image getImage(Dimension triangleSize) {
        if (triangleSize.equals(bufferedImageSize))
            return bufferedImage;
        bufferedImage = drawTriangle(triangleSize);
        bufferedImageSize = triangleSize;
        return bufferedImage;
    }

    @Override
    public Image getResizeImage(Dimension size) {
        BufferedImage bufferedImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gBuffer = (Graphics2D) bufferedImage.getGraphics();
        gBuffer.setColor(Color.LIGHT_GRAY);
        gBuffer.fillRect(0, 0, size.width, size.height);
        return bufferedImage;
    }
}
