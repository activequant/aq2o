from pysimplesoap.client import SoapClient
from datetime import datetime


# instantiate a new client object. 
client = SoapClient(wsdl="http://54.245.112.94:9999/main?WSDL",trace=False,soap_server="oracle", ns=False, namespace=False)

# call the random number method. 
print client.randomNumber()['return']

# add two numbers
print client.add(b=16780, a=20678768)['return']

# some instrument related calls. 
print client.instrumentCount()['return']
print client.instrumentKeys()

#
instId = client.instrumentKeys()[0]['return']
print instId

#
print client.loadInstrument(primaryKey=instId)

#  storing a time series value. format see http://docs.python.org/2/library/datetime.html#strftime-and-strptime-behavior
dt1 = datetime.strptime("20120101", "%Y%m%d")
dt2 = datetime.strptime("20120102", "%Y%m%d")

client.saveTimeSeriesValue("ACCOUNT_0", "RAW", long(dt1.strftime('%s%f')) * 1000, "TOTAL_EQUITY", 10.0)
client.saveTimeSeriesValue("ACCOUNT_0", "RAW", long(dt2.strftime('%s%f')) * 1000, "TOTAL_EQUITY", 12.0)


# now let's fetch it. 
response = client.getTimeSeries("ACCOUNT_0", "TOTAL_EQUITY", "RAW", "19700101", "20200101")

for i in range(0,len(response)):
  timeStamp = response[i]['return'][0]['item']
  val = response[i]['return'][1]['item']
  print timeStamp, " -> ", val

  
# let's store some more data ... 
for i in range(10,30):
  dt1 = datetime.strptime("201206" + str(i),  "%Y%m%d")
  client.saveTimeSeriesValue("GXZ1", "RAW", long(dt1.strftime('%s%f')) * 1000, "DEMARK_INDICATOR", i*i)





