import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.iteck.trader.localbusiness.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;
import java.util.*;


public class CoverageArea extends AppCompatActivity {
    private MapView map = null;
    private ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
    private Button zoomLevel;
    private TextView zoomLevelText;
    MyLocationNewOverlay myLocation;
    private GeoPoint homePoint;
    private IMapController mapController;

    private Polygon polygon;
    private List<GeoPoint> geoPoints;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setMap();
        setToolbar();
        setZoomText();

        setAreaPoints();

        setHome();
        setShop1();
        setShop2();
        setShop3();
        setShop4();
        setShop5();

        setMapControls();

    }

    private void setAreaPoints() {

        geoPoints = new ArrayList<>();
        geoPoints.add(new GeoPoint(8.180992, 77.336551));
        geoPoints.add(new GeoPoint(8.183966, 77.340353));
        geoPoints.add(new GeoPoint(8.179836, 77.336105));
        geoPoints.add(new GeoPoint(8.178744, 77.339179));
        geoPoints.add(new GeoPoint(8.182155, 77.341925));
        geoPoints.add(new GeoPoint(8.1816555, 77.339318));

        polygon = new Polygon();    //see note below
        polygon.setFillColor(Color.parseColor("#80FFE082"));
        polygon.setStrokeColor(Color.parseColor("#FFD54F"));
        polygon.setStrokeWidth(5f);
        convexHull(geoPoints, geoPoints.size());
        map.getOverlayManager().add(polygon);
        map.invalidate();

    }

    private void setZoomText() {
        zoomLevel = (Button) findViewById(R.id.zoom_level);
        zoomLevelText = (TextView) findViewById(R.id.zoom_level_text);
        zoomLevelText.setText("Zoom Level: " + (int)map.getZoomLevelDouble());

    }

    private void setShop5() {
        Marker shop5 = new Marker(map);
        shop5.setPosition(new GeoPoint(8.183966, 77.340353));
        map.getOverlays().add(shop5);
        shop5.setIcon(getResources().getDrawable(R.drawable.ic_place_red_24dp));
        shop5.setTitle("Shop 5");

    }

    private void setShop4() {
        Marker shop4 = new Marker(map);
        shop4.setPosition(new GeoPoint(8.179836, 77.336105));
        map.getOverlays().add(shop4);
        shop4.setIcon(getResources().getDrawable(R.drawable.ic_place_red_24dp));
        shop4.setTitle("Shop 4");
    }

    private void setShop3() {
        Marker shop3 = new Marker(map);
        shop3.setPosition(new GeoPoint(8.180992, 77.336551));
        map.getOverlays().add(shop3);
        shop3.setIcon(getResources().getDrawable(R.drawable.ic_place_red_24dp));
        shop3.setTitle("Shop 3");
    }

    private void setShop2() {
        Marker shop2 = new Marker(map);
        shop2.setPosition(new GeoPoint(8.178744, 77.339179));
        map.getOverlays().add(shop2);
        shop2.setIcon(getResources().getDrawable(R.drawable.ic_place_red_24dp));
        shop2.setTitle("Shop 2");
    }

    private void setShop1() {
        Marker shop1 = new Marker(map);
        shop1.setPosition(new GeoPoint(8.184297, 77.338105));
        map.getOverlays().add(shop1);
        shop1.setIcon(getResources().getDrawable(R.drawable.ic_place_red_24dp));
        shop1.setTitle("Shop 1");
    }

    private void setMapControls() {
        map.setBuiltInZoomControls(false);
        map.setMultiTouchControls(true);
        map.setVerticalMapRepetitionEnabled(false);
        map.setHorizontalMapRepetitionEnabled(false);
        map.setMinZoomLevel(8.0);
        map.setMaxZoomLevel(18.0);

        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(CoverageArea.this,items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        //do something
                        return true;
                    }
                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return false;
                    }
                });
        mOverlay.setFocusItemsOnTap(true);

        map.getOverlays().add(mOverlay);
    }

    private void setHome() {
        mapController = map.getController();
        mapController.setZoom(15.0);

        homePoint = new GeoPoint(8.182155, 77.341925);
        mapController.setCenter(homePoint);

        Marker homeMarker = new Marker(map);
        homeMarker.setPosition(homePoint);
        homeMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(homeMarker);

        homeMarker.setIcon(getResources().getDrawable(R.drawable.ic_place_green_24dp));
        homeMarker.setTitle("Home");

        //double finger rotation
        map.getOverlays().add(new RotationGestureOverlay(map));


        MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                Toast.makeText(getBaseContext(),p.getLatitude() + " - "+p.getLongitude(),Toast.LENGTH_LONG).show();

                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };


        MapEventsOverlay OverlayEvents = new MapEventsOverlay(getBaseContext(), mReceive);
        map.getOverlays().add(OverlayEvents);

    }

    private void setMap() {
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_nearby_shops);

        map = (MapView) findViewById(R.id.mapview);

        OnlineTileSourceBase CUSTOM = new XYTileSource("Mapnik",
                0, 19, 256, ".png", new String[] {
                "http://a.tile.openstreetmap.org/",
                "http://b.tile.openstreetmap.org/",
                "http://c.tile.openstreetmap.org/" },"© OpenStreetMap contributors");
        map.setTileSource(CUSTOM);

    }

    public void onResume(){
        super.onResume();
        map.onResume();
    }

    public void onPause(){
        super.onPause();
        map.onPause();
    }

    public void displayZoomLevel(View view) {
        zoomLevelText.setText("Zoom Level: " + map.getZoomLevelDouble());
    }

    public void goHome(View view) {

        Toast.makeText(this, "Getting Current Location. Please wait.", Toast.LENGTH_SHORT).show();

        myLocation = new MyLocationNewOverlay(map);
        myLocation.enableFollowLocation();
        Drawable person = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_dot_blue, null);
        Bitmap bPerson = null;
        if (person != null) {
            bPerson = ((BitmapDrawable) person).getBitmap();
        }
        Drawable arrow = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_navigation_blue_24dp, null);
        Bitmap bArrow = null;
        if (person != null) {
            bArrow = ((BitmapDrawable) arrow).getBitmap();
        }
        myLocation.enableMyLocation();
        myLocation.setDrawAccuracyEnabled(true);
        myLocation.setPersonIcon(bPerson);
        myLocation.setDirectionArrow(bArrow, bArrow);

        map.getOverlays().add(myLocation);

    }

    private void setToolbar() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Nearby Shops");
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

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

}
