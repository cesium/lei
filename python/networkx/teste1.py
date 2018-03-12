import json
import networkx as nx
import matplotlib.pyplot as plt
import math
import sys
from pprint import pprint
import copy

json_file = open("../../estruturaDados.txt")
json_data = json.load(json_file)
json_file.close()
antes = copy.deepcopy(json_data)
for disciplina in json_data:
    uc = json_data[disciplina]
    lista_turnos = []
    lista_arestas = []
    for troca in uc:
        origem = troca["origem"]
        destino = troca["destino"]
        if origem not in lista_turnos:
            lista_turnos.append(origem)
        if destino not in lista_turnos:
            lista_turnos.append(destino)
        existe_aresta = False
        for aresta in lista_arestas:
            if aresta[0] == origem and aresta[1] == destino:
                aresta[2].append(troca["data"])
                aresta[2].sort()
                existe_aresta = True
                break
        if not existe_aresta:
            lista_arestas.append((origem,destino,[troca["data"]]))
    # CONTRUIR O GRAFO
    grafo = nx.DiGraph()
    grafo.add_nodes_from(lista_turnos)
    for aresta in lista_arestas:
        grafo.add_edge(aresta[0],aresta[1],weight=min(aresta[2]))
    # DESENHAR O GRAFO
    '''
    pos=nx.spring_layout(grafo,scale=2,k=0.2,iterations=10)
    nx.draw(grafo,pos,with_labels=True)
    labels = nx.get_edge_attributes(grafo,'weight')
    nx.draw_networkx_edge_labels(grafo,pos,edge_labels=labels,font_size=5,rotate=False)
    plt.show()'''

    #ENCONTRAR CICLOS
    lista_ciclos = list(nx.simple_cycles(grafo))
    if len(lista_ciclos) > 0 :
        maior_ciclo = max(list(map(len,lista_ciclos)))
        lista_ciclos = list(filter(lambda x : len(x) == maior_ciclo , lista_ciclos))
        existe_empate = len(lista_ciclos) > 1
        if existe_empate:
            for ciclo in lista_ciclos:
                ciclo.append(ciclo[0])
                pass
        else:
            ciclo = lista_ciclos[0]
            
            ciclo.append(ciclo[0])
            print(ciclo)
            print("ANTES:")
            pprint(uc)
            for nodo in ciclo[:-1]:
                
                (aluno,data) = ("",sys.maxsize)
                for troca in uc:
                    aluno_troca = troca["aluno"]
                    
                    origem = troca["origem"]
                    destino = troca["destino"]
                    data_troca = troca["data"]
                    '''print("aluno: " + str(aluno) )
                    print("aluno_troca: " + str(aluno_troca) )
                    print("data: " + str(data) )
                    print("data_troca: " + str(data_troca) )'''
                    
                    if origem == nodo and destino == ciclo[ciclo.index(nodo)+1]:
                        print("EXISTE ARESTA")
                        if data_troca < data:
                            print("DATA MENOR")
                            (aluno,data) = (aluno_troca,data_troca)
                
                uc.remove(list(filter(lambda x : x["aluno"] == aluno,uc))[0])
            
            print("DEPOIS:")
            pprint(uc)

print("ANTES")
pprint(antes)
print("DEPOIS")
pprint(json_data)
    
    

