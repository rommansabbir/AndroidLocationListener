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
| v1.1          |
| v1.0.1        |
| v1.0          |


# Usages

### For Java: 

```Kotlin
class MainActivity : AppCompatActivity(), PermissionCallback {

    override fun onPermissionRequest(isGranted: Boolean) {
        /**
         * Check if granted or not
         */
        if(isGranted){
		/**
		 * Get location after a period of time
		 */
            LocationListener.getLocationPeriodic(5000, object : LocationCallback {
                override fun onLocationSuccess(location: Location) {
                    Toast.makeText(this@MainActivity, "${location.latitude}, ${location.longitude}", Toast.LENGTH_SHORT).show()
                }
            })
		/**
		 * Get location for a single time
		 */
            LocationListener.getLocation(object : LocationCallback{
                override fun onLocationSuccess(location: Location) {
                    Toast.makeText(this@MainActivity, "${location.latitude}, ${location.longitude}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /**
         * Set LocationListener component with activity
         */
        LocationListener.setComponent(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        /**
         * Pass to the LocationListener
         */
        LocationListener.processResult(requestCode, permissions, grantResults)
    }

    override fun onDestroy() {
        super.onDestroy()
        /**
         * Self Destroy LocationListener
         */
        LocationListener.selfDestroy()
    }
}

```


### Contact me
[Portfolio](https://www.rommansabbir.com/) | [LinkedIn](https://www.linkedin.com/in/rommansabbir/) | [Twitter](https://www.twitter.com/itzrommansabbir/) | [Facebook](https://www.facebook.com/itzrommansabbir/)

