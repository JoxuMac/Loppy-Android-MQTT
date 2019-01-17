from mqtt import MQTTClient
import pycom
import sys
import time

from pysense import Pysense
from SI7006A20 import SI7006A20 		# Humidity and Temperature Sensor
from LTR329ALS01 import LTR329ALS01 	# Digital Ambient Light Sensor
from raw2lux import raw2Lux             # ... additional library for the light sensor

import ufun

wifi_ssid = 'IOT-JA'
wifi_passwd = 'iotja2018'
broker_addr = 'iot.eclipse.org'
dev_id = 'lopytest'

RED = 0xFF0000
BLUE = 0x0000FF
YELLOW = 0xFFFF33
GREEN = 0x007F00
PINK =  0xFF6EC7
OFF = 0x000000

def set_led_to(color=GREEN):
    pycom.heartbeat(False) # Disable the heartbeat LED
    pycom.rgbled(color)

def get_temperature():
	return tempHum.temp()
	
def get_humidity():
	return tempHum.humidity()
	
def get_light():
	return raw2Lux(ambientLight.lux())

def on_message(topic, msg):
	# SET TOPIC
	if str(topic)==str("b'set'"):
		# RED LED COLOR
		if str(msg)==str("b'red'"):
			set_led_to(RED)
			print("LED COLOR: RED")
		else:
		# YELLOW LED COLOR
			if str(msg)==str("b'yellow'"):
				set_led_to(YELLOW)
				print("LED COLOR: YELLOW")
			else:
		# GREEN LED COLOR
				if str(msg)==str("b'green'"):
					set_led_to(GREEN)
					print("LED COLOR: GREEN")
				else:
		# BLUE LED COLOR
					if str(msg)==str("b'blue'"):
						set_led_to(BLUE)
						print("LED COLOR: BLUE")
					else:
		# OFF LED COLOR
						if str(msg)==str("b'off'"):
							set_led_to(OFF)
							print("LED COLOR: OFF")
		# COLOR INVALID
						else:
							print("MENSAJE INVALIDO RECIBIDO: ", msg)
	else:
	# GET TOPIC
		if str(topic)==str("b'get'"):
			# GET TEMPERATURE
			if str(msg)==str("b'temp'"):
				the_data = get_temperature()
				client.publish('temp', str(the_data))
				print("TEMPERATURA: ", str(the_data))
			else:
				# GET HUMIDITY
				if str(msg)==str("b'hum'"):
					the_data = get_humidity()
					client.publish('hum', str(the_data))
					print("HUMEDAD: ", str(the_data))
				else:
					# GET LIGHT
					if str(msg)==str("b'lig'"):
						the_data = get_light()
						client.publish('lig', str(the_data))
						print("LUZ: ", str(the_data))
					# INVALID MSG
					else:
						print("MENSAJE INVALIDO RECIBIDO: ", msg)
	# OTHER TOPIC
		else:
			print("TOPIC INVALIDO RECIBIDO: ", topic)

ufun.connect_to_wifi(wifi_ssid, wifi_passwd)

client = MQTTClient(dev_id, broker_addr, 1883)
client.set_callback(on_message)

print ("Connecting to broker: " + broker_addr)
try:
	client.connect()
except OSError:
	print ("Cannot connect to broker: " + broker_addr)
	sys.exit()	
print ("Connected to broker: " + broker_addr)

client.subscribe('get')
client.subscribe('set')

py = Pysense()
tempHum = SI7006A20(py)
ambientLight = LTR329ALS01(py) 

set_led_to(PINK);
time.sleep(5)
set_led_to(OFF);

print('Waiting ...')
while 1:
    client.check_msg()
