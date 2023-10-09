import java.util.*;

public class TwoClosest {


    static LinkedList<Point> s = new LinkedList<>();

    static void merge(Point[] arr, int l, int mid, int r, boolean sortByX) {
        int n1 = mid - l + 1;
        int n2 = r - mid;
        Point[] L = new Point[n1];
        Point[] R = new Point[n2];
        for (int i = 0; i < n1; ++i) {
            L[i] = arr[l + i];
        }
        for (int j = 0; j < n2; ++j) {
            R[j] = arr[mid + 1 + j];
        }
        int i = 0, j = 0, k = l;
        while (i < n1 && j < n2) {
            if ((sortByX && L[i].x <= R[j].x) || (!sortByX && L[i].y <= R[j].y)) {
                arr[k] = L[i];
                ++i;
            } else {
                arr[k] = R[j];
                ++j;
            }
            ++k;
        }
        while (i < n1) {
            arr[k] = L[i];
            ++i;
            ++k;
        }
        while (j < n2) {
            arr[k] = R[j];
            ++j;
            ++k;
        }
    }

    static void mergeSort(Point[] arr, int l, int r, boolean sortByX) {
        if (l < r) {
            int mid = (l + r) / 2;
            mergeSort(arr, l, mid, sortByX);
            mergeSort(arr, mid + 1, r, sortByX);
            merge(arr, l, mid, r, sortByX);
        }
    }

    static double closestUtil(Point[] Px, Point[] Py, int n) {
        if (n <= 3) {
            return bruteForce(Px, n);
        }

        int mid = n / 2;
        Point midPoint = Px[mid];

        Point[] Pyl = new Point[mid];
        Point[] Pyr = new Point[n - mid];
        int li = 0, ri = 0;

        for (int i = 0; i < n; ++i) {
            if (Py[i].x < midPoint.x || (Py[i].x == midPoint.x && Py[i].y < midPoint.y)) {
                Pyl[li++] = Py[i];
            } else {
                Pyr[ri++] = Py[i];
            }
        }

        double dl = closestUtil(Px, Pyl, mid);
        double dr = closestUtil(Arrays.copyOfRange(Px, mid, n), Pyr, n - mid);
        double d = Math.min(dl, dr);

        Point[] strip = new Point[n];
        int j = 0;
        for (int i = 0; i < n; ++i) {
            if (Math.abs(Py[i].x - midPoint.x) < d) {
                strip[j++] = Py[i];
            }
        }

        return stripClosest(strip, j, d);
    }

    static double min = 1e9;


    static double dist(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }


    static double stripClosest(Point[] strip, int size, double d) {

        for (int i = 0; i < size; ++i) {
            for (int j = i + 1; j < size && (strip[j].y - strip[i].y) < d; ++j) {
                if (dist(strip[i], strip[j]) < min) {
                    min = dist(strip[i], strip[j]);
                    s.clear();
                    s.add(strip[i]);
                    s.add(strip[j]);
                }
                if(j==i+7)break;
            }
        }
        return min;
    }


    static double closest(Point[] P, int n) {
        Point[] Px = Arrays.copyOf(P, n);
        Point[] Py = Arrays.copyOf(P, n);
        mergeSort(Px, 0, n - 1, true);
        mergeSort(Py, 0, n - 1, false);
        return closestUtil(Px, Py, n);
    }

    static double bruteForce(Point[] P, int n) {


        for (int i = 0; i < n; ++i) {
            for (int j = i + 1; j < n; ++j) {
                if (dist(P[i], P[j]) < min) {

                    s.clear();
                    s.add(P[j]);
                    s.add(P[i]);
                    min = dist(P[i], P[j]);
                }
            }
        }
        return min;
    }

    public static void main(String args[]) {


        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        Point[] P = new Point[a];
        for (int i = 0; i < a; i++) {
            P[i] = new Point(scanner.nextLong(), scanner.nextLong());

        }

        closest(P, a);

        if (s.get(0).x < s.get(1).x)
            System.out.println((int)s.get(0).x + " " +(int) s.get(0).y + " " + (int)s.get(1).x + " " + (int)s.get(1).y);

        else System.out.println((int)s.get(1).x + " " + (int)s.get(1).y + " " + (int)s.get(0).x + " " + (int)s.get(0).y);
    }

    static class Point {
        double x, y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}