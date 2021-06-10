# IncidentMapper
Simple utility to create and view incident reports in Realtime. This app makes use of Android Jetpack libraries and Firestore database.

## Features
* View incidents on a map in realtime
* Add incident
* Incident upload caching

## Details
* A single Activity application, conforming to the MVVM application architecture.
* Consists of a MapView Fragment as the main view under the screens package. Calls to the web are made through Firestore database and supports caching. 
* Map view is implemented using the Google Maps service.

## Screens
<table border="0">
 <tr>
    <td><b style="font-size:30px">Splash Screen</b></td>
   <td><b style="font-size:30px">Welcome View</b></td>
    <td><b style="font-size:30px">Incident Screen</b></td>
    <td><b style="font-size:30px">Add Incident View</b></td>
 </tr>
 <tr>
    <td>
     <img src=https://github.com/dims26/IncidentMapper/blob/main/screenshots/splash_screen.jpg width="180" height="400" />
   </td>
   <td>
    <img src=https://github.com/dims26/IncidentMapper/blob/main/screenshots/welcome_dialog.jpg width="180" height="400" />
  </td>
   <td>
    <img src=https://github.com/dims26/IncidentMapper/blob/main/screenshots/view_incidents.jpg width="180" height="400" />
  </td>
   <td>
    <img src=https://github.com/dims26/IncidentMapper/blob/main/screenshots/add_incident.jpg width="180" height="400" />
  </td>
 </tr>
 <tr>
  <td>Splash Screen</td>
  <td>Welcome dialog and introduction.</td>
  <td>Main view showing all incidents.</td>
  <td>Add new incident.</td>
 </tr>
 </table>

## Built using

* [Android Jetpack](https://developer.android.com/jetpack/?gclid=Cj0KCQjwhJrqBRDZARIsALhp1WQBmjQ4WUpnRT4ETGGR1T_rQG8VU3Ta_kVwiznZASR5y4fgPDRYFqkaAhtfEALw_wcB) - Official suite of libraries, tools, and guidance to help developers write high-quality apps.
  * [Android KTX](https://developer.android.com/kotlin/ktx)
  * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
  * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
  * [Navigation](https://developer.android.com/jetpack/androidx/releases/navigation)
* [Firestore Database](https://firebase.google.com/docs/firestore) - NoSQL cloud database to store and sync data for client- and server-side development.
* [Hilt](https://dagger.dev/hilt/) - Dependency injection library
