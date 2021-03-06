package com.mancel.yann.go4lunch;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.mancel.yann.go4lunch.models.Details;
import com.mancel.yann.go4lunch.models.DistanceMatrix;
import com.mancel.yann.go4lunch.models.NearbySearch;
import com.mancel.yann.go4lunch.models.Restaurant;
import com.mancel.yann.go4lunch.repositories.PlaceRepository;
import com.mancel.yann.go4lunch.repositories.PlaceRepositoryImpl;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Yann MANCEL on 18/12/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch
 *
 * Test on {@link PlaceRepository}.
 */
@RunWith(AndroidJUnit4.class)
public class PlaceRepositoryTest {

    // FIELDS --------------------------------------------------------------------------------------

    private PlaceRepository mPlaceRepository = new PlaceRepositoryImpl();

    private static final String KEY = InstrumentationRegistry.getInstrumentation()
                                                             .getTargetContext()
                                                             .getResources()
                                                             .getString(R.string.google_maps_key);

    // METHODS -------------------------------------------------------------------------------------

    // -- Simple streams --

    @Test
    public void should_Fetch_NearbySearch() {
        // Creates Observable
        final Observable<NearbySearch> observable = this.mPlaceRepository.getStreamToFetchNearbySearch("45.9922027,4.7176896",
                                                                                                       200.0,
                                                                                                       "restaurant",
                                                                                                        KEY);

        // Creates Observer
        final TestObserver<NearbySearch> observer = new TestObserver<>();

        // Creates Stream
        observable.subscribeWith(observer)
                  .assertNoErrors()
                  .assertNoTimeout()
                  .awaitTerminalEvent();

        // Fetches the result
        final NearbySearch nearbySearch = observer.values().get(0);

        // TEST: results's size
        assertEquals("results: Number of restaurant equals to 13", nearbySearch.getResults().size(), 13);

        // TEST: results[0]
        assertEquals("results[0] > [place_id]", nearbySearch.getResults().get(0).getPlaceId(), "ChIJn_OajiGF9EcRQFJ9o2prZ3w");
        assertEquals("results[0] > [name]", nearbySearch.getResults().get(0).getName(), "Charcutier Traiteur Vigne");
        assertEquals("results[0] > [vicinity]", nearbySearch.getResults().get(0).getVicinity(), "269 Rue nationale, Villefranche-sur-Saône");
        assertTrue("results[0] > geometry > location > [lat]", nearbySearch.getResults().get(0).getGeometry().getLocation().getLat() == 45.992902);
        assertTrue("results[0] > geometry > location > [lng]", nearbySearch.getResults().get(0).getGeometry().getLocation().getLng() == 4.718997);

        // TEST: [status]
        assertEquals("[status]", nearbySearch.getStatus(), "OK");
    }

    @Test
    public void should_Fetch_Details() {
        // Creates Observable
        final Observable<Details> observable = this.mPlaceRepository.getStreamToFetchDetails("ChIJp7JQEyKF9EcR72wJu3P1fUw",
                                                                                              KEY);

        // Creates Observer
        final TestObserver<Details> observer = new TestObserver<>();

        // Creates Stream
        observable.subscribeWith(observer)
                  .assertNoErrors()
                  .assertNoTimeout()
                  .awaitTerminalEvent();

        // Fetches the result
        final Details details = observer.values().get(0);

        // TEST: results > [name]
        assertEquals("results > [name]", details.getResult().getName(), "Pizzeria Al Dente");

        // TEST: results > geometry > location > [lat & lng]
        assertTrue("results > geometry > location > [lat]", details.getResult().getGeometry().getLocation().getLat() == 45.99138300000001);
        assertTrue("results > geometry > location > [lng]", details.getResult().getGeometry().getLocation().getLng() == 4.718076);

        // TEST: results > address_components[0 & 1] > [short_name]
        assertEquals("results > address_components[0] > [short_name]", details.getResult().getAddressComponents().get(0).getShortName(), "166");
        assertEquals("results > address_components[1] > [short_name]", details.getResult().getAddressComponents().get(1).getShortName(), "Rue Dechavanne");

        // TEST: results > [website]
        assertEquals("results > [website]", details.getResult().getWebsite(), "http://pizzeria-villefranche-sur-saone.fr/");

        // TEST: [status]
        assertEquals("[status]", details.getStatus(), "OK");
    }

    @Test
    public void should_Fetch_DistanceMatrix() {
        // Creates Observable
        final Observable<DistanceMatrix> observable = this.mPlaceRepository.getStreamToFetchDistanceMatrix("45.9922027,4.7176896",
                                                                                                           "45.99138300000001,4.718076",
                                                                                                           "walking",
                                                                                                           "metric",
                                                                                                            KEY);

        // Creates Observer
        final TestObserver<DistanceMatrix> observer = new TestObserver<>();

        // Creates Stream
        observable.subscribeWith(observer)
                  .assertNoErrors()
                  .assertNoTimeout()
                  .awaitTerminalEvent();

        // Fetches the result
        final DistanceMatrix distanceMatrix = observer.values().get(0);

        // TEST: results > rows[0] > elements[0] > distance > [text]
        assertEquals("distance > [text]", distanceMatrix.getRows().get(0).getElements().get(0).getDistance().getText(), "90 m");

        // TEST: results > rows[0] > elements[0] > duration > [text]
        assertEquals("duration > [text]", distanceMatrix.getRows().get(0).getElements().get(0).getDuration().getText(), "1 min");

        // TEST: [status]
        assertEquals("[status]", distanceMatrix.getStatus(), "OK");
    }

    // -- Complex streams --

    @Test
    public void should_Fetch_Details_And_DistanceMatrix() {
        // Creates Observable
        final Observable<Restaurant> observable;
        observable= this.mPlaceRepository.getStreamToFetchDetailsAndDistanceMatrix("45.9922027,4.7176896",
                                                                                   "ChIJp7JQEyKF9EcR72wJu3P1fUw",
                                                                                   "walking",
                                                                                   "metric",
                                                                                    KEY);

        // Creates Observer
        final TestObserver<Restaurant> observer = new TestObserver<>();

        // Creates Stream
        observable.subscribeWith(observer)
                  .assertNoErrors()
                  .assertNoTimeout()
                  .awaitTerminalEvent();

        // Fetches the result
        final Restaurant restaurant = observer.values().get(0);

        // TEST: [Details] > results > [name]
        assertEquals("results > [name]", restaurant.getDetails().getResult().getName(), "Pizzeria Al Dente");

        // TEST: [DistanceMatrix] > results > rows[0] > elements[0] > distance > [text]
        assertEquals("distance > [text]", restaurant.getDistanceMatrix().getRows().get(0).getElements().get(0).getDistance().getText(), "90 m");

        // TEST: [Details] >[status]
        assertEquals("[status]", restaurant.getDetails().getStatus(), "OK");
        assertEquals("[status]", restaurant.getDistanceMatrix().getStatus(), "OK");
    }

    @Test
    public void should_Fetch_NearbySearch_Then_should_Fetch_Restaurants() {
        // Creates Observable
        final Observable<List<Restaurant>> observable;
        observable = this.mPlaceRepository.getStreamToFetchNearbySearchThenToFetchRestaurants("45.9922027,4.7176896",
                                                                                              200.0,
                                                                                              "restaurant",
                                                                                              "walking",
                                                                                              "metric",
                                                                                               KEY);

        // Creates Observer
        final TestObserver<List<Restaurant>> observer = new TestObserver<>();

        // Creates Stream
        observable.subscribeWith(observer)
                  .assertNoErrors()
                  .assertNoTimeout()
                  .awaitTerminalEvent();

        // Fetches the result
        final List<Restaurant> restaurants = observer.values().get(0);

        // TEST: results's size
        assertEquals("results: Number of restaurant equals to 13", restaurants.size(), 13);
    }
}
