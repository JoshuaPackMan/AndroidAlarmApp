# AndroidAlarmApp
A custom alarm app that allows you to use Google Play Music as your alarm music. I developed this after discovering that 
my Samsung Galaxy's default alarm app didn't allow you to use Google Play Music as the alarm music. The way the app works is
analogous to how bluetooh speakers or headphones work - they usually have a play button that sends
out a general signal to your device to play whatever music is available. That is what this app does -
it broadcasts an intent that asks the device to play music when the alarm goes off. In order for this to work you need to
set the alarm using the button and timepicker in the app, then you need to open the Google Play Music app. Play and then pause 
the song at the position you want the alarm to start playing the music at. Then lock your screen and go to bed. Do not open another
app before the alarm goes off or the alarm will not work.
