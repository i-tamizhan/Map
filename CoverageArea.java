public static int orientation(GeoPoint p, GeoPoint q, GeoPoint r) {
        double val = (q.getLongitude() - p.getLongitude()) * (r.getLatitude() - q.getLatitude()) -
                (q.getLatitude() - p.getLatitude()) * (r.getLongitude() - q.getLongitude());

        if (val == 0) return 0;  // collinear
        return (val > 0) ? 1 : 2; // clock or counterclock wise
    }

    public  void convexHull(List <GeoPoint> hullPoints, int n) {
        // There must be at least 3 points
        if (n < 3) return;

        // Initialize Result
        Vector<GeoPoint> hull = new Vector<GeoPoint>();

        // Find the leftmost point
        int l = 0;
        for (int i = 1; i < n; i++)
            if (hullPoints.get(i).getLatitude() < hullPoints.get(l).getLatitude())
                l = i;

        // Start from leftmost point, keep moving
        // counterclockwise until reach the start point
        // again. This loop runs O(h) times where h is
        // number of points in result or output.
        int p = l, q;
        do {
            // Add current point to result
            hull.add(hullPoints.get(p));

            // Search for a point 'q' such that
            // orientation(p, x, q) is counterclockwise
            // for all points 'x'. The idea is to keep
            // track of last visited most counterclock-
            // wise point in q. If any point 'i' is more
            // counterclock-wise than q, then update q.
            q = (p + 1) % n;

            for (int i = 0; i < n; i++) {
                // If i is more counterclockwise than
                // current q, then update q
                if (orientation(hullPoints.get(p), hullPoints.get(i), hullPoints.get(q))
                        == 2)
                    q = i;
            }

            // Now q is the most counterclockwise with
            // respect to p. Set p as q for next iteration,
            // so that q is added to result 'hull'
            p = q;

        } while (p != l);  // While we don't come to first
        // point

        // Print Result
        for (GeoPoint temp : hull) {
            //System.out.println("(" + temp.getLatitude() + ", " + temp.getLongitude() + ")");
            hullPoints.add(temp);
            System.out.println("(" + temp.getLatitude() + ", " + temp.getLongitude() + ")");

        }

        hullPoints = sortVerticies(hullPoints);
        hullPoints.add(hullPoints.get(0));

        polygon.setPoints(hullPoints);
        polygon.setTitle("A sample polygon");
    }


    public GeoPoint findCentroid(List<GeoPoint> cGeoPoints) {
        double x = 0.0;
        double y = 0.0;
        for (GeoPoint gP : cGeoPoints) {
            x += gP.getLatitude();
            y += gP.getLongitude();
        }
        GeoPoint center = new GeoPoint(0.0, 0.0);
        center.setLatitude(x / geoPoints.size());
        center.setLongitude(y / geoPoints.size());

        return center;

    }

    public List<GeoPoint> sortVerticies(List<GeoPoint> sortPoints) {
        // get centroid
        GeoPoint center = findCentroid(sortPoints);
        Collections.sort(sortPoints, (a, b) -> {
            double a1 = (Math.toDegrees(Math.atan2(a.getLatitude() - center.getLatitude(), a.getLongitude() - center.getLongitude())) + 360) % 360;
            double a2 = (Math.toDegrees(Math.atan2(b.getLatitude() - center.getLatitude(), b.getLongitude() - center.getLongitude())) + 360) % 360;
            return (int) (a1 - a2);
        });
        return sortPoints;
    }
