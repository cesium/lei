import json
import networkx as nx
import matplotlib.pyplot as plt
import math
import sys
from pprint import pprint
import copy
import time

def getMinDataCiclo(grafo,ciclo,minData):
    minDataCiclo = sys.maxsize
    for nodo in ciclo[:-1]:
        data_aresta = grafo[nodo][ciclo[ciclo.index(nodo)+1]]["weight"]
        naoConta = False
        if data_aresta > minData and data_aresta < minDataCiclo:
            minDataCiclo = data_aresta
    if minDataCiclo == sys.maxsize:
        print("ESTE CICLO NAO TEM NENHUMA ARESTA COM MENOR PESO!!!")
        return minData
    return minDataCiclo

antes = time.time()
json_file = open("../../estruturaDados_v2.txt")
json_data = json.load(json_file)
json_file.close()
for disciplina in json_data:
    uc = json_data[disciplina]
    lista_turnos = []
    lista_arestas = []
    for troca in uc:
        origem = troca["from_shift_id"]
        destino = troca["to_shift_id"]
        if origem not in lista_turnos:
            lista_turnos.append(origem)
        if destino not in lista_turnos:
            lista_turnos.append(destino)
        existe_aresta = False
        for aresta in lista_arestas:
            if aresta[0] == origem and aresta[1] == destino:
                aresta[2].append(troca["created_at"])
                aresta[2].sort()
                existe_aresta = True
                break
        if not existe_aresta:
            lista_arestas.append((origem,destino,[troca["created_at"]]))
    # CONTRUIR O GRAFO
    grafo = nx.DiGraph()
    grafo.add_nodes_from(lista_turnos)
    for aresta in lista_arestas:
        grafo.add_edge(aresta[0],aresta[1],weight=min(aresta[2]))
    # DESENHAR O GRAFO
    
    pos=nx.spring_layout(grafo,scale=2,k=0.2,iterations=10)
    nx.draw(grafo,pos,with_labels=True)
    labels = nx.get_edge_attributes(grafo,'weight')
    nx.draw_networkx_edge_labels(grafo,pos,edge_labels=labels,font_size=8,rotate=False)
    plt.show()
    
    
    #ENCONTRAR TODOS CICLOS
    lista_ciclos = list(nx.simple_cycles(grafo))
    if len(lista_ciclos) > 0 :
        maior_ciclo = max(list(map(len,lista_ciclos)))
        # SELECIONAR APENAS OS CICLOS DE MAIOR TAMANHO
        lista_ciclos = list(filter(lambda x : len(x) == maior_ciclo , lista_ciclos))
        for ciclo in lista_ciclos:
            ciclo.append(ciclo[0])
        print("\n\n--------Trocas na uc: " +disciplina.upper()+ "-----------");
        print("Todos os maiores ciclos:")
        pprint(lista_ciclos)
        print("")
        existe_empate = len(lista_ciclos) > 1
        ciclo_a_resolver = []
        if existe_empate:
            # ENCONTRAR CICLO A SER RESOLVIDO
            
            minData = 0
            minDataCiclos = {}
            desempatado = False
            maxIters = len(grafo)-1
            iteracao=0
            while True:
                minDataCiclos = {}
                for ciclo in lista_ciclos:
                    minDataCiclo = getMinDataCiclo(grafo,ciclo,minData)
                    ciclosComTamanhoX = minDataCiclos.get(minDataCiclo,-1)
                    if ciclosComTamanhoX != -1:
                        ciclosComTamanhoX.append(ciclo)
                    else:
                        minDataCiclos[minDataCiclo]=[ciclo]
                #pprint(minDataCiclos)
                #input()
                minData = min(minDataCiclos.keys())
                desempatado = len(minDataCiclos[minData]) == 1
                if(not desempatado):
                    lista_ciclos = minDataCiclos[minData]


                
                iteracao = iteracao+1
                if desempatado or iteracao == maxIters:
                    break
            if iteracao == maxIters:
                print("\nmaxIters atingido!!!\n")
            ciclo_a_resolver = minDataCiclos[minData][0]
        else:
            ciclo_a_resolver = lista_ciclos[0]
        
        print("Ciclo a resolver:")
        pprint(ciclo_a_resolver)
        print("")
        for nodo in ciclo_a_resolver[:-1]:
               
            (idTroca,data) = ("",sys.maxsize)
            for pedidoTroca in uc:
                troca = pedidoTroca["id"]
                
                origem = pedidoTroca["from_shift_id"]
                destino = pedidoTroca["to_shift_id"]
                data_troca = pedidoTroca["created_at"]                
                if origem == nodo and destino == ciclo_a_resolver[ciclo_a_resolver.index(nodo)+1]:
                    if data_troca < data:
                        (idTroca,data) = (troca,data_troca)
            troca_a_remover = list(filter(lambda x : x["id"] == idTroca,uc))[0]
            print(troca_a_remover)
            uc.remove(troca_a_remover)
        print("------------------------------------------")
depois = time.time()
print("Tempo decorrido: " + str(int((depois-antes)*1000)) + " milissegundos")
            
