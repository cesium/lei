c=1
for i in range(1,8):
    for j in list(filter(lambda x: x!=i, range(1,8))):
        print("{")
        print('\t"id": "a'+str(c)+'",')
        print('\t"from_shift_id": "TP'+str(i)+'",')
        print('\t"to_shift_id": "TP'+str(j)+'",')
        print('\t"created_at": '+str(i))
        print('},')
        c+=1