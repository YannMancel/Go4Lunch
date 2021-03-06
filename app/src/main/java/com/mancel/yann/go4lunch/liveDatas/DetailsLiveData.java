package com.mancel.yann.go4lunch.liveDatas;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.crashlytics.android.Crashlytics;
import com.mancel.yann.go4lunch.models.Details;

import io.reactivex.Observable;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by Yann MANCEL on 15/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.liveDatas
 *
 * A {@link LiveData<Details>} subclass.
 */
public class DetailsLiveData extends LiveData<Details> {

    // FIELDS --------------------------------------------------------------------------------------

    @Nullable
    private Disposable mDisposable = null;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor by default
     */
    public DetailsLiveData() {}

    // METHODS -------------------------------------------------------------------------------------

    // -- LiveData --

    @Override
    protected void onActive() {
        super.onActive();
    }

    @Override
    protected void onInactive() {
        super.onInactive();

        // Disposes the Disposable
        if (this.mDisposable != null && !this.mDisposable.isDisposed()) {
            this.mDisposable.dispose();
        }
    }

    // -- Details --

    /**
     * Gets the details with {@link Observable<Details>}
     * @param observable an {@link Observable<Details>}
     */
    public void getDetailsWithObservable(@NonNull final Observable<Details> observable) {
        // Creates stream
        this.mDisposable = observable.subscribeWith(new DisposableObserver<Details>() {
            @Override
            public void onNext(Details details) {
                // Notify
                setValue(details);
            }

            @Override
            public void onError(Throwable e) {
                Crashlytics.log(Log.ERROR,
                                DetailsLiveData.class.getSimpleName(),
                               "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                // Do nothing
            }
        });
    }
}
