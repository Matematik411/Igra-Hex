# Igra Hex

- Projektna naloga pri predmetu **Programiranje II**
- Avtorja: **Andraž Maier** in **Nejc Zajc**
- Mentorja: **prof. dr. Alex Simpson** in **asist. dr. Matjaž Zaveršnik**
- Študijsko leto **2019/2020**

##### **Kratek opis delovanje AI.**

AI deluje prek algoritma Minimax z Alphabeta dodatno. Ocena pozicije, ki je glavna funkcija Minimaxa deluje prek dveh funkcij. 
Prva raèuna dinamièno in upošteva, da sta dva žetona iste barve povezana, èe je vmes eno polje prazno, sta v eni izmed treh zmagovalnih smeri 
in sta obe poti do žetona prazni. To pomeni, da nam nasprotnik ne more pokvariti povezave. Èe se odloèi za prvo možnost, mi povežemo po drugi in obratno. 
To omogoèa še enkrat boljše igranje kot navadna ocena poti z BFS algoritmom, kot smo jo videli na predavanjih. Vendar pa ima ta funkcija eno pomanjkljivost.
Ne prepozna poti, ki ne gredo le v optimalno smer, paè pa tudi malo nazaj. Zato imava še drugo funkcijo za oceno pozicije, ki deluje s pomoèjo BFS algoritma
in se sproži, èe AI zazna, da take poti lahko obstajajo. Tako AI med igro izbira med dvema funkcijama odvisno od tega katera se mu v danem trenutku zdi boljša.