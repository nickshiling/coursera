import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;

    public SeamCarver(Picture picture) {
        if (picture ==  null) {
            throw new IllegalArgumentException();
        }

        this.picture = new Picture(picture);
    }

    public Picture picture() {
        return new Picture(picture);
    }

    public int width() {
        return picture.width();
    }

    public int height() {
        return picture.height();
    }

    public double energy(int x, int y) {
        validatePixelX(x);
        validatePixelY(y);

        return energyPicture(this.picture, x, y);
    }

    private static double energyPicture(Picture pict, int x, int y) {
        int width = pict.width();
        int height = pict.height();

        if (x == 0 || x == width - 1 || y == 0 || y == height -1) {
            return 1000;
        }

        int leftPixelColor = pict.getRGB(x - 1, y);
        int rightPixelColor = pict.getRGB(x + 1, y);

        double xEnergy = Math.pow(((leftPixelColor >> 16) & 0xFF) - ((rightPixelColor >> 16) & 0xFF), 2) +
                Math.pow(((leftPixelColor >> 8) & 0xFF) - ((rightPixelColor >> 8) & 0xFF), 2) +
                Math.pow(((leftPixelColor) & 0xFF) - ((rightPixelColor) & 0xFF), 2);

        leftPixelColor = pict.getRGB(x, y - 1);
        rightPixelColor = pict.getRGB(x, y + 1);

        double yEnergy = Math.pow(((leftPixelColor >> 16) & 0xFF) - ((rightPixelColor >> 16) & 0xFF), 2) +
                Math.pow(((leftPixelColor >> 8) & 0xFF) - ((rightPixelColor >> 8) & 0xFF), 2) +
                Math.pow(((leftPixelColor) & 0xFF) - ((rightPixelColor) & 0xFF), 2);

        return Math.sqrt(xEnergy + yEnergy);
    }

    private void validatePixelX(int x) {
        if (x < 0 || x >= width()) {
            throw new IllegalArgumentException();
        }
    }

    private void validatePixelY(int y) {
        if (y < 0 || y >= height()) {
            throw new IllegalArgumentException();
        }
    }

    public int[] findHorizontalSeam() {
        Picture transposed = transpose(this.picture);

        int[] seam = findVerticalSeamPicture(transposed);

        return seam;
    }

    private static Picture transpose(Picture origin) {
        Picture result = new Picture(origin.height(), origin.width());

        for (int x = 0; x < origin.width(); x++) {
            for (int y = 0; y < origin.height(); y++) {
                result.set(y, x, origin.get(x, y));
            }
        }

        return result;
    }

    public int[] findVerticalSeam() {
        return findVerticalSeamPicture(this.picture);
    }

    private static int[] findVerticalSeamPicture(Picture pict) {
        int width = pict.width();
        int height = pict.height();

        double[][] energy = new double[width][height];
        double[][] distanceTo = new double[width][height];
        int[][] pathTo = new int[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                energy[x][y] = energyPicture(pict, x, y);
                distanceTo[x][y] = Double.POSITIVE_INFINITY;
                pathTo[x][y] = -1;
            }
        }

        for (int x = 0; x < width; x++) { distanceTo[x][0] = 0; }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < Math.min(height - 1, i + 1); j++) {
                int x = i-j;
                int y = j;
                if (x > 0) relax(pathTo, distanceTo, x, y, x - 1, y + 1, energy[x][y]);
                relax(pathTo, distanceTo, x, y, x, y + 1, energy[x][y]);
                if (x < width - 1) relax(pathTo, distanceTo, x, y, x + 1, y + 1, energy[x][y]);
            }
        }

        for (int i = 1; i < height - 1; i++) {
            for (int j = 0; j < Math.min(width, height - i - 1); j++) {
                int x = width - j - 1;
                int y = i + j;

                if (x > 0) relax(pathTo, distanceTo, x, y, x - 1, y + 1, energy[x][y]);
                relax(pathTo, distanceTo, x, y, x, y + 1, energy[x][y]);
                if (x < width - 1) relax(pathTo, distanceTo, x, y, x + 1, y + 1, energy[x][y]);
            }
        }

        int minVertical = 0;
        double minPath = Double.POSITIVE_INFINITY;

        int bottomRow = height - 1;

        for (int i = 0; i < width; i++) {
            if (distanceTo[i][bottomRow] < minPath) {
                minPath = distanceTo[i][bottomRow];
                minVertical = i;
            }
        }

        int[] seam = new int[height];
        seam[bottomRow] = minVertical;

        for (int i = bottomRow; i > 0; i--) {
            seam[i-1] = pathTo[seam[i]][i];
        }

        return seam;
    }

    private static void relax(int[][] pathTo, double[][] distanceTo, int fromX, int fromY, int toX, int toY, double weight) {
        double newDistanceTo = distanceTo[fromX][fromY] + weight;
        if (distanceTo[toX][toY] > newDistanceTo) {
            distanceTo[toX][toY] = newDistanceTo;
            pathTo[toX][toY] = fromX;
        }
    }

    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || height() <= 1 || seam.length != width()) {
            throw new IllegalArgumentException();
        }

        validateSeamDistance(seam);

        Picture newPicture = new Picture(width(), height() - 1);

        int newY;

        for (int x = 0; x < width(); x++) {
            newY = 0;
            for (int y = 0; y < height(); y++) {
                if (seam[x] == y) {
                    continue;
                }

                newPicture.set(x, newY, picture.get(x, y));
                newY++;
            }
        }
        picture = newPicture;
    }

    private static void validateSeamDistance(int[] seam) {
        for (int i = 1; i < seam.length; i++) {
            if (Math.abs(seam[i-1] - seam[i]) > 1) {
                throw new IllegalArgumentException();
            }
        }
    }

    public void removeVerticalSeam(int[] seam) {
        if (seam == null || width() <= 1 || seam.length != height()) {
            throw new IllegalArgumentException();
        }

        validateSeamDistance(seam);

        Picture newPicture = new Picture(width() -1, height());

        int newX;

        for (int y = 0; y < height(); y++) {
            newX = 0;
            for (int x = 0; x < width(); x++) {
                if (seam[y] == x) {
                    continue;
                }

                newPicture.set(newX, y, picture.get(x, y));
                newX++;
            }
        }

        picture = newPicture;
    }
}
