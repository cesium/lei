import json
import networkx as nx
import matplotlib.pyplot as plt




json_file = open("../../estruturaDados.txt")
json_data = json.load(json_file)
json_file.close()


for disciplina in json_data:
    uc = json_data[disciplina]
    lista_turnos = []
    lista_arestas = []
    for troca in json_data[disciplina]:
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
        grafo.add_edge(aresta[0],aresta[1],object=aresta[2],weight=min(aresta[2]))
    # DESENHAR O GRAFO
    pos=nx.circular_layout(grafo)
    nx.draw(grafo,with_labels=True)
    labels = nx.get_edge_attributes(grafo,'weight')
    nx.draw_networkx_edge_labels(grafo,pos,edge_labels=labels)
    plt.show()
    print(list(nx.simple_cycles(grafo)))
    
    
