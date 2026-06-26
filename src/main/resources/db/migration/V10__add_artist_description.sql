ALTER TABLE artists ADD COLUMN IF NOT EXISTS description TEXT;

ALTER TABLE artists ADD COLUMN IF NOT EXISTS description TEXT;

UPDATE artists SET description = 'Pulitzer Prize-winning rapper from Compton, California, widely regarded as one of the most influential hip-hop artists of his generation.' WHERE name = 'Kendrick Lamar';
UPDATE artists SET description = 'Canadian rapper, singer and songwriter from Toronto who became one of the best-selling music artists of the 2010s.' WHERE name = 'Drake';
UPDATE artists SET description = 'American R&B singer-songwriter known for her genre-blending sound and the acclaimed albums Ctrl and SOS.' WHERE name = 'SZA';
UPDATE artists SET description = 'The psychedelic music project of Australian multi-instrumentalist Kevin Parker, blending dreamy pop with retro electronic textures.' WHERE name = 'Tame Impala';
UPDATE artists SET description = 'English rock band from Sheffield whose debut became the fastest-selling debut album in British chart history.' WHERE name = 'Arctic Monkeys';
UPDATE artists SET description = 'American heavy metal band formed in Los Angeles in 1981, among the most commercially successful and influential metal acts of all time.' WHERE name = 'Metallica';
UPDATE artists SET description = 'American singer-songwriter who rose to fame as a teenager with a distinctive whisper-pop sound and introspective lyrics.' WHERE name = 'Billie Eilish';
UPDATE artists SET description = 'British rock band from London known for their atmospheric anthems and status as one of the best-selling acts of the 21st century.' WHERE name = 'Coldplay';