# Identifies the children in households who are starting school this year and who have been customers in the last 3 months

# Version 1.0.0
# Date 2026-06-24

# 
#Berlin 2026
# Am 1. August werden alle Kinder schulpflichtig, die in der Zeit zwischen 
# dem 1. Oktober bis zum 30. September des Folgejahres 6 Jahre alt werden.
#
#please change the dates for other years or states
  
select a.haushaltId,a.gdatum,a.vName,a.nName,b.anz_einkauf,b.letzer_einkauf from familienmitglied a
  join 
    (select kunde, 
        COUNT(*) AS anz_einkauf,
        MAX(erfassungsZeit) as letzer_einkauf
    from einkauf 
    group by kunde
    ) b     
    on a.haushaltId = b.kunde
    where add_months(a.gdatum,6*12) between '2025-10-01' and '2026-09-30'
        and haushaltId in 
        (select kunde from einkauf 
            where erfassungsZeit between add_months(now(),-3) and now()
                  and storniertAm is null)
    order by a.haushaltId;

select c.haushaltId,count(*) as anz_childs from
   (select haushaltId from familienmitglied 
     where add_months(gdatum,6*12) between '2025-10-01' and '2026-09-30'
        and haushaltId in 
        (select kunde from einkauf 
            where erfassungsZeit between add_months(now(),-3) and now()
                  and storniertAm is null)
    ) c
    group by c.haushaltId;  