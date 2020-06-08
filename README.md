# Igra Hex

- Projektna naloga pri predmetu **Programiranje II**
- Avtorja: **Andra� Maier** in **Nejc Zajc**
- Mentorja: **prof. dr. Alex Simpson** in **asist. dr. Matja� Zaver�nik**
- �tudijsko leto **2019/2020**

##### **Kratek opis delovanje AI.**

AI deluje prek algoritma Minimax z Alphabeta dodatno. Ocena pozicije, ki je glavna funkcija Minimaxa deluje prek dveh funkcij. 
Prva ra�una dinami�no in upo�teva, da sta dva �etona iste barve povezana, �e je vmes eno polje prazno, sta v eni izmed treh zmagovalnih smeri 
in sta obe poti do �etona prazni. To pomeni, da nam nasprotnik ne more pokvariti povezave. �e se odlo�i za prvo mo�nost, mi pove�emo po drugi in obratno. 
To omogo�a �e enkrat bolj�e igranje kot navadna ocena poti z BFS algoritmom, kot smo jo videli na predavanjih. Vendar pa ima ta funkcija eno pomanjkljivost.
Ne prepozna poti, ki ne gredo le v optimalno smer, pa� pa tudi malo nazaj. Zato imava �e drugo funkcijo za oceno pozicije, ki deluje s pomo�jo BFS algoritma
in se spro�i, �e AI zazna, da take poti lahko obstajajo. Tako AI med igro izbira med dvema funkcijama odvisno od tega katera se mu v danem trenutku zdi bolj�a.