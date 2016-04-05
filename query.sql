select * from Attraction limit 10
select * from Review limit 10
select * from Attractionreviews



select attractionid, attractionreviews.reviewid, 
	json_agg(
	json_build_object(
		'Aid',attractionid, 
		'Rid',Review.reviewid, 
		'RAuthor', Review.author,
		'RTitle',Review.title,
		'Rdate', Review.date,
		'Rrating',Review.rating,
		'Rwc',Review.wordcount,
		'Rcontent',Review.content
       )) AS v -- into Temp
FROM  
Review inner Join Attractionreviews
on attractionreviews.reviewid = Review.reviewid 
GROUP BY attractionid, attractionreviews.reviewid;
--Order by num;

select a.attractionid, Attractionreviews.reviewid, a.num into ARcount
from
(
	select attractionid, count(*) as num 
	from Attractionreviews
	group by attractionid
	order by num desc
) as a
join Attractionreviews
on a.attractionid = Attractionreviews.attractionid



select attractionid, Review.reviewid, 
	json_agg(
	json_build_object(
		'Aid',attractionid, 
		'Rid',Review.reviewid, 
		'Rauthor', Review.author,
		'Rtitle',Review.title,
		'Rdate', Review.date,
		'Rrating',Review.rating,
		'Rwc',Review.wordcount,
		'Rcontent',Review.content
       )) AS v into Json
FROM  
Review inner Join ARcount
on ARcount.reviewid = Review.reviewid 
GROUP BY attractionid,Review.reviewid,ARcount.num
order by ARcount.num desc;


select * from Json



select * from Review where reviewid = 'r221401208'
select * from Attractionreviews where reviewid = 'r221401208'	--d2149128

select * from Attraction where attractionid = 'd2149128'
