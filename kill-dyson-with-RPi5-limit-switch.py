import time
from gpiozero import Button
from signal import pause
from libdyson.cloud import DysonAccount
from libdyson import DEVICE_TYPE_PURE_COOL_LINK_DESK
from libdyson.const import ENVIRONMENTAL_INIT, AirQualityTarget
from libdyson.dyson_device import DysonInvalidCredential
from libdyson.dyson_pure_cool_link import DysonPureCoolLink
from libdyson.dyson_pure_hot_cool_link import DysonPureHotCoolLink
from requests.auth import AuthBase, HTTPBasicAuth

# Define the GPIO pin connected to the limit switch
LIMIT_SWITCH_PIN = 2

# Create a Button object for the limit switch
limit_switch = Button(LIMIT_SWITCH_PIN)

# #Do Dyson Stuff:
# serial = 'C2A-US-NNC3106B'
# credential = 'xfBpzACkf2FNCOywOVkleTrSjUkUYEi26l0gjK0JDV7KO7rIRYM1zTVNQcrgIoOfxLieMzK0PGNdOJkfSUA1HA=='
# product_type = '475'
# dpcl = DysonPureCoolLink(serial, credential, product_type)
# ip = "192.168.86.21"
# dpcl.connect(ip)

email = "j******@****.com"
password = "xxxxxxxx"

account = DysonAccount()
otpcheck = account.login_email_otp(email, "US")
otp_input = input("Please enter the OTP: ")
otp_result = otpcheck(otp_input, password)
devices = account.devices()
fan = devices.pop()

serial = fan.serial
credential = fan.credential
product_type = fan.product_type

dphcl = DysonPureHotCoolLink(serial, credential, product_type)
ip2 = "192.168.86.42"
DysonPureHotCoolLink.connect(dphcl, ip2)

# Define a function to be called when the limit switch state changes
def switch_state_changed():
    if limit_switch.is_pressed:
        print("Limit switch closed")
        time.sleep(0.5)


    else:
        print("Limit switch open")
        dphcl.turn_off()
        time.sleep(0.5)

# Attach the function to the 'when_pressed' and 'when_released' events of the limit switch
limit_switch.when_pressed = switch_state_changed
limit_switch.when_released = switch_state_changed

# Keep the program running to continue monitoring the limit switch
# pause()
while True:
    switch_state_changed()
