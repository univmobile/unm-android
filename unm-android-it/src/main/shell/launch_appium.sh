#!/bin/sh

export PATH="/home/dandriana/.local/bin:${PATH}"

ps -A | grep node || appium &
