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
    
    pos=nx.spring_layout(grafo,scale=2,k=0.2,iterations=10)
    nx.draw(grafo,pos,with_labels=True)
    labels = nx.get_edge_attributes(grafo,'weight')
    nx.draw_networkx_edge_labels(grafo,pos,edge_labels=labels,font_size=5,rotate=False)
    plt.show()

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
                
                if origem == nodo and destino == ciclo_a_resolver[ciclo_a_resolver.index(nodo)+1]:
                    if data_troca < data:
                        (aluno,data) = (aluno_troca,data_troca)
            aluno_a_remover = list(filter(lambda x : x["aluno"] == aluno,uc))[0]
            print(aluno_a_remover)
            uc.remove(aluno_a_remover)
            
            
'''
print("ANTES")
pprint(antes)
print("DEPOIS")
pprint(json_data)
'''
    

