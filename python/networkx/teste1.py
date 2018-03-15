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
    '''
    pos=nx.spring_layout(grafo,scale=2,k=0.2,iterations=10)
    nx.draw(grafo,pos,with_labels=True)
    labels = nx.get_edge_attributes(grafo,'weight')
    nx.draw_networkx_edge_labels(grafo,pos,edge_labels=labels,font_size=5,rotate=False)
    plt.show()
    '''
    #ENCONTRAR TODOS CICLOS
    lista_ciclos = list(nx.simple_cycles(grafo))
    print("Todos os ciclos encontrados:")
    pprint(lista_ciclos)
    print("\n\n")
    if len(lista_ciclos) > 0 :
        maior_ciclo = max(list(map(len,lista_ciclos)))
        # SELECIONAR APENAS OS CICLOS DE MAIOR TAMANHO
        lista_ciclos = list(filter(lambda x : len(x) == maior_ciclo , lista_ciclos))
        print("Todos os maiores ciclos:")
        pprint(lista_ciclos)
        print("\n\n")
        existe_empate = len(lista_ciclos) > 1
        ciclo_a_resolver = []
        if existe_empate:
            # ENCONTRAR CICLO A SER RESOLVIDO
            (min_data, min_ciclo) = (sys.maxsize,[])
            for ciclo in lista_ciclos:
                ciclo.append(ciclo[0])
                min_data_ciclo = sys.maxsize
                for nodo in ciclo[:-1]:
                    data_aresta = grafo[nodo][ciclo[ciclo.index(nodo)+1]]["weight"]
                    if data_aresta < min_data_ciclo:
                        min_data_ciclo = data_aresta
                if min_data_ciclo < min_data:
                    (min_data , min_ciclo) = (min_data_ciclo,ciclo)
            ciclo_a_resolver = min_ciclo
        else:
            ciclo_a_resolver = lista_ciclos[0]
            ciclo_a_resolver.append(ciclo_a_resolver[0])
        print("Ciclo a resolver:")
        pprint(ciclo_a_resolver)
        print("\n\n")
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
            
            
'''
print("ANTES")
pprint(antes)
print("DEPOIS")
pprint(json_data)
'''
    

