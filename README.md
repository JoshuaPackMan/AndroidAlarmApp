# AndroidAlarmApp
A custom alarm app that allows you to use Google Play Music or Spotify Music as your alarm music. 
Music must be paused in the notifcation bar for the alarm to work. The way the app works is
analogous to how bluetooh speakers or headphones work - they usually have a play button that sends
out a general signal to your device to play whatever music is available. That is what this app does -
it sends a broadcast an intent that asks the device to play music when the alarm goes off. This is 
why you need to have the song you want to use as your alarm paused in your navigation bar - this
allows the broadcasted intent to find a song to play. 
