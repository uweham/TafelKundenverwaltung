SET @monat:=1 ;
SET @jahr :=2026;

# Version 1.0.2 2026-01-15



# Statistik

select DATE(A.erfassungsZeit) as DATUM, 
        D.name,
        F.NAME AS bescheidname, 
        count(*) as anzahlHaushalte, 
        sum(A.anzahlErwachsene) as anzahlErwachsene,
        sum(A.anzahlKinder) as anzahlKinder from einkauf A 
    inner join
        haushalt B 
            on A.kunde = B.kundennummer
    inner join
        familienmitglied C
            on A.person = C.personID
    inner join
        nation D
            on C.nation = D.nationID 
    left join
        bescheid E 
            on C.personID=E.personID and 
              DATE(A.erfassungsZeit) BETWEEN E.gueltigAb AND E.gueltigBis
    left join 
        bescheidart F 
            on E.bescheidArtId=F.bescheidArtId 
        where month(A.erfassungsZeit)=@monat   and
              year(A.erfassungsZeit) =@jahr    and 
              isnull(storniertAm)                
        group by DATUM,bescheidname,NAME 
        INTO OUTFILE '/data_export/tmp/Statistik_Teil1.csv'  
        FIELDS TERMINATED BY ';' OPTIONALLY ENCLOSED BY '"'
        LINES TERMINATED BY '\n' ;   

# Alleinerziehende
# je Haushalt ein Erwachsener und mind. ein Kind

select DATE(erfassungsZeit) as DATUM,count(*) as anzahlAlleinerz 
    from einkauf 
    where anzahlErwachsene=1 AND anzahlKinder>0
          and month(erfassungsZeit)=@monat   and
              year(erfassungsZeit) =@jahr    and 
              isnull(storniertAm) 
        group by DATUM                    
        INTO OUTFILE '/data_export/tmp/Statistik_Teil2.csv'  
        FIELDS TERMINATED BY ';' OPTIONALLY ENCLOSED BY '"'
        LINES TERMINATED BY '\n' ;   


# Neuanmeldungen (Haushalte)

select A.kundeSeit as Datum, count(*) as anzahlHaushalte from haushalt A
    where month(A.kundeSeit)=@monat   and
        year(A.kundeSeit) =@jahr  
            group by Datum 
        INTO OUTFILE '/data_export/tmp/Statistik_Teil3.csv'  
        FIELDS TERMINATED BY ';' OPTIONALLY ENCLOSED BY '"'
        LINES TERMINATED BY '\n' ;   
