import matplotlib.pyplot as plt
import numpy as np

time, X, Y = np.loadtxt('export/brusselator/alchemistdata.txt', unpack = True)

plt.title('Brusselator')
plt.xlabel('time')
plt.ylabel('concentration')
plt.plot(time, X, 'r', time, Y, 'b')
plt.legend(['X', 'Y'])
plt.show()