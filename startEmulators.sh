#!/usr/bin/env bash

cd ${ANDROID_HOME}/tools/
./emulator -avd AndroidTestDevice &
./bin/uiautomatorviewer &
xcrun instruments -w "iPhone X (11.1) [96B8FCE3-DE91-4B5B-B446-D86DEF801B3C] (Simulator)"
