#!/usr/bin/env python
# coding: utf-8
import pandas as pd
import matplotlib.pyplot as plt
from pandas.core.frame import DataFrame
from mpl_toolkits.mplot3d import axes3d
import matplotlib.cm as cm
from sklearn.model_selection import train_test_split
import numpy as np
import random
import math


#recuperamos los datos almacenados de la base de datos
df = pd.read_csv('durazno.csv')

# visualizamos DataFrame a partir del csv
df.shape

# calculamos estadísticas básica de todos los datos de las 5 columnas:
df.describe()

#obtenemos los nombres unicos del registro
fname = df['nombre'].unique()
#print(fname[0:2])

#calculamos la cantidad de registos por nombre de fruta
fsize = df.groupby('nombre',sort=False).size()
fsize
df.head(5)

# graficamos en barras la  cantidad de frutas por tipo (durazno = naranja, nectarina = amarillo)
plt.bar(fname,fsize,color = ['orange','yellow'])
plt.xlabel('Fruit Name')
plt.ylabel('Fruit Count')
plt.title('Fruits with Color Dataset')
plt.show()


# imprimimos un gráfico con ejes x, y, z para ver la dispersión de las frutas en función a sus etiquetas
fig = plt.figure()
ax = fig.add_subplot(111,projection='3d') 
ax.scatter(df['rojo'],df['verde'],df['amarillo'])
ax.set_xlabel('Red')
ax.set_ylabel('Green')
ax.set_zlabel('Yellow')
plt.show()




#establecemos k = a 5 dimenciones para el uso de kdtree

k = 5
class Node:
    def __init__(self,point,axis,clase):
        self.point = point
        self.left = None
        self.right = None
        self.axis = axis
        self.clase = clase

#función que permite saber la altura del arbol
def getHeight(node):
    if(node==None):
        return 0
    else:
        return max(getHeight(node.left),getHeight(node.right)) + 1

#función que construye el arbol
def build_kdtree(points,depth=0):
    n = len(points)
    axis = depth % k
    if(n<=0):
        return None
    if(n==1):
        return Node(points[0],axis,points[0][k])
    points.sort(key=lambda point: point[axis])
    median = len(points)//2

    node = Node(points[median],axis,points[median][k])
    node.left = build_kdtree(points[:median],depth + 1)
    node.right = build_kdtree(points[median+1:], depth + 1)
    #print("build")
    return node

#calculo de la distancia de dos puntos
def distanceSquared(point1,point2):
    distance = 0
    for i in range(k):
        distance+=math.pow(point1[i]-point2[i],2)
    return math.sqrt(distance)

#ordena la data mediante una cola
def order(queue):
    for i in range (len(queue)):
        j = i+1
        for j in range (len(queue)):
            if (queue[i][0]>queue[j][0]) :
                temp=queue[i]
                queue[i]=queue[j]
                queue[j]=temp

#función que define la cola de prioridad
def priority_queue(queue,point,count):
    if (len(queue)<count):
        queue.append(point)
        sorted(queue,key=lambda point: point[0])
    else:
        for i in range (len(queue)):
            if (queue[i][0]>point[0]):
                temp=queue[i]
                queue[i]=point
                point=temp

#función que busca al vecino k más cercano  
def Knearest(node,point,k):
    quever=[]
    k_nearest_point(node,point,quever,k);
    print(quever)
    indices=[]
    for i in range (len(quever)):
        indices.append(quever[i][1])
    clases= []
    tipo=""
    for nodo in indices:
        clase = nodo[5]
        if clase =='nectarina':
            tipo="nectarina"
        if clase == 'durazno':
            tipo="durazno"
        clases.append(tipo)
    return clases

#función que calcula al vecino más cercano 
def k_nearest_point( node , point , queue, k):
    if ( node == None ):
        return
    axis = node.axis
    dist=distanceSquared(point,node.point)
    priority_queue(queue,[dist,node.point],k)
    next_branch = None
    opposite_branch = None
    if (point[axis]<node.point[axis]):
        next_branch=node.left
        opposite_branch=node.right
    else:
        next_branch=node.right
        opposite_branch=node.left
    k_nearest_point(next_branch,point,queue,k)
    if(len(queue)<k or queue[0][0]>abs(point[axis]-node.point[axis])):
        k_nearest_point(opposite_branch,point,queue,k)

#función que determina al valor más frecuente de la lista        
def most_frequent(List): 
    return max(set(List), key = List.count)

Y=df['nombre']
D1=df['diametro']
D2=df['peso']
R=df['rojo']
G=df['verde']
B=df['amarillo']

X=[D1,D2,R,G,B,Y]
Points=[]
for i in range(10000):
    aux=(X[0][i],X[1][i],X[2][i],X[3][i],X[4][i],X[5][i])
    Points.append(aux)
X=Points

#usamos la librería sklearn únicamente con el fin de segmentar  data para entrenamiento y prueba
x_train,x_test = train_test_split(X,random_state = 0)

#se instancia un objeto kdtree con la data de entrenamiento
kdtree=build_kdtree(x_train)
#imprimirmos la cantidad seleccionada de data de entrenamiento
print("Datos de Entrenamiento : ",len(x_train))
#imprimirmos la cantidad seleccionada de data de prueba
print("Datos de Prueba : ",len(x_test))

#se instancia un objeto queue para tener el vecino k cercano
queue=Knearest(kdtree,x_test[5],2)

#evaluamos si el mas frecuente de la cola es igual a los datos de la prueba
if (most_frequent(queue)==x_test[4][4]):
    #imprimimos el mas frecuente de la cola
    print("Result : ",most_frequent(queue))

#inicializamos en 0 los casos satisfactorios
success_cases=0

# iteramos con el fin de encontrar la exactitud (accuracy) siempre que el mas frecuente
# sea igual a los datos de prueba
for i in range(len(x_test)):
    if (most_frequent(Knearest(kdtree,x_test[i],2))==x_test[i][5]):
        success_cases+=1
#imprimimos la exactitud de la selección con 2 valores
print("Accuracy : ",success_cases/len(x_test))

success_cases=0
for i in range(len(x_test)):
    if (most_frequent(Knearest(kdtree,x_test[i],10))==x_test[i][5]):
        success_cases+=1
#imprimimos la exactitud de la selección con 10 valores
print("Accuracy : ",success_cases/len(x_test))


success_cases=0
for i in range(len(x_test)):
    if (most_frequent(Knearest(kdtree,x_test[i],20))==x_test[i][5]):
        success_cases+=1
#imprimimos la exactitud de la selección con 20 valores
print("Accuracy : ",success_cases/len(x_test))


def randrange(n, vmin, vmax):
    return (vmax - vmin)*np.random.rand(n) + vmin

fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')

n = 100

for c, m, zlow, zhigh in [('r', 'o', -50, -25), ('b', '^', -30, -5)]:
    xs = randrange(n, 23, 32)
    ys = randrange(n, 0, 100)
    zs = randrange(n, zlow, zhigh)
    ax.scatter(xs, ys, zs, c=c, marker=m)

ax.set_xlabel('X Label')
ax.set_ylabel('Y Label')
ax.set_zlabel('Z Label')

plt.show()