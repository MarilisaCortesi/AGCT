import matplotlib.pyplot as plt
import numpy as np

time, TetR, LacI, AcI = np.loadtxt('export/repressilator/alchemistdata.txt', unpack = True)

plt.title('Repressilator')
plt.xlabel('time')
plt.ylabel('concentration')
plt.plot(time, TetR, 'r', time, LacI, 'g', time, AcI, 'b')
plt.legend(['TetR', 'LacI', 'λcI'])
plt.show()