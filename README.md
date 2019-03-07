# LocationListener-Android

## Documentation

### Installation
---
Step 1. Add the JitPack repository to your build file 

```gradle
	allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}
```

Step 2. Add the dependency

```gradle
	dependencies {
	        implementation 'com.github.rommansabbir:LocationListener-Android:Tag'
	}
```

---

### Version available

| Releases        
| ------------- |
| v1.0.1	      |
| v1.0          |


# Usages

### For Java: 

```java
public class MainActivity extends AppCompatActivity implements
        LocationListener.LocationListenerCallbackInterface {
    private LocationListener locationListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Instantiate LocationListener
         * Pass the context of the parent class
         * @param context
         */
        locationListener = new LocationListener(this);

        /**
         * Request for runtime permission if permission isn't available
         */
        locationListener.requestLocationPermission();

        /**
         * This method is responsible for return location object
         */
        locationListener.getLocation();

        /**
         * This method is responsible for return location object after a specific interval
         * This method accept only "long" value and interval time should be in milli second
         * @param intervalTimeMillis
         */
        locationListener.getLocationPeriodic(10000);
    }


    @Override
    public void onLocationSuccess(Location location) {
        //TODO Implement your logic here
    }

    @Override
    public void onLocationFailure(String ERROR_MESSAGE) {
        //TODO Implement your login here
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * Destroy the callback after it usages for fail safe
         */
        locationListener.destroyCallback();
    }
}
```


### Contact me
[Portfolio](https://www.rommansabbir.com/) | [LinkedIn](https://www.linkedin.com/in/rommansabbir/) | [Twitter](https://www.twitter.com/itzrommansabbir/) | [Facebook](https://www.facebook.com/itzrommansabbir/)

