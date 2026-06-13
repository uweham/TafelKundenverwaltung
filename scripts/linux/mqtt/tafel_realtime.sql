

select DATE(erfassungsZeit) as DATUM, 
	curtime() as time,
        count(*) as anzahlHaushalte, 
        sum(anzahlErwachsene) as anzahlErwachsene,
        sum(anzahlKinder) as anzahlKinder, 
	sum(anzahlErwachsene+anzahlKinder) as anzahlPersonen from einkauf  
	where isnull(storniertAm) and DATE(erfassungsZeit) = CURDATE()               
        group by DATUM ;
