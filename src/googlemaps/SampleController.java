/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package googlemaps;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

/**
 *
 * @author diego
 */
public class SampleController implements Initializable {

    private JSObject doc;
    private EventHandler<MapEvent> onMapLatLngChanged;
    private boolean ready = false;
    private WebEngine wengine;
    @FXML
    private WebView wview;

    private void initCommunication() {
        wengine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(final ObservableValue<? extends Worker.State> observableValue,
                    final Worker.State oldState,
                    final Worker.State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    doc = (JSObject) wengine.executeScript("window");
                    doc.setMember("app", SampleController.this);
                }
            }
        });
    }

    private void invokeJS(final String str) {
        if (ready) {
            doc.eval(str);
        } else {
            wengine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
                @Override
                public void changed(final ObservableValue<? extends Worker.State> observableValue,
                        final Worker.State oldState,
                        final Worker.State newState) {
                    if (newState == Worker.State.SUCCEEDED) {
                        doc.eval(str);
                    }
                }
            });
        }
    }

    public void setOnMapLatLngChanged(EventHandler<MapEvent> eventHandler) {
        onMapLatLngChanged = eventHandler;
    }

    public void handle(double lat, double lng) {
        if (onMapLatLngChanged != null) {
            MapEvent event = new MapEvent(this, lat, lng);
            onMapLatLngChanged.handle(event);
        }
    }

    public void setMarkerPosition(double lat, double lng) {
        String sLat = Double.toString(lat);
        String sLng = Double.toString(lng);
        invokeJS("setMarkerPosition(" + sLat + ", " + sLng + ")");
    }

    public void setMapCenter(double lat, double lng) {
        String sLat = Double.toString(lat);
        String sLng = Double.toString(lng);
        invokeJS("setMapCenter(" + sLat + ", " + sLng + ")");
    }

    public void switchSatellite() {
        invokeJS("switchSatellite()");
    }

    public void switchRoadmap() {
        invokeJS("switchRoadmap()");
    }

    public void switchHybrid() {
        invokeJS("switchHybrid()");
    }

    public void switchTerrain() {
        invokeJS("switchTerrain()");
    }

    public void startJumping() {
        invokeJS("startJumping()");
    }

    public void stopJumping() {
        invokeJS("stopJumping()");
    }

    public void setHeight(double h) {
        wview.setPrefHeight(h);
    }

    public void setWidth(double w) {
        wview.setPrefWidth(w);
    }

    public ReadOnlyDoubleProperty widthProperty() {
        return wview.widthProperty();
    }
    
    @FXML
    private void mapZoomIn() {
//        invokeJS("map.setZoom(15)");
        invokeJS("linha()");
    }

    @FXML
    private void pog_nada() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        wengine = wview.getEngine();
        wengine.load(getClass().getResource("map.html").toExternalForm());

        wengine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> ov, Worker.State t, Worker.State t1) {
                if (t1 == Worker.State.SUCCEEDED) {
                    ready = true;
                }
            }
        });

        initCommunication();
//        setMarkerPosition(0,0);
//        setMapCenter(0, 0);
//        switchTerrain();
    }
}
