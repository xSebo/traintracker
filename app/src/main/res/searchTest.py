import pendulum
from datetime import datetime

# Small script to search file for station codes, gives idea of how long a linear search takes.

file = open("stations.csv", "r")
newArray = file.read().split(",")
file.close()

speedTest = str(input("Enter a station: "))

now = datetime.now()
current_time = str(now.strftime("%H:%M:%S"))
t1 = pendulum.parse(current_time)

for idx in range(0,len(newArray),2):
    if str(speedTest.lower()) in str(newArray[idx].lower()):
        print(newArray[idx] + ", " + newArray[idx+1])

current_time = str(now.strftime("%H:%M:%S"))
t2 = pendulum.parse(current_time)
period = t2 - t1
print("Time taken: "+ str(period.seconds) + "s")