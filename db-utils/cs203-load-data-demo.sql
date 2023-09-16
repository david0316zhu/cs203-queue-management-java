use ticketmaster;
## Add users
insert into ticketmaster.user (user_id, mobile_number, email, password, authenticator_id, is_verified) values 
(1, "06586616057", "david.zhu.2022@smu.edu.sg", "test123","001", 1),
(2, "06597873327", "ryan.yap.2022@smu.edu.sg", "test123", "002", 1),
(3, "06598231539", "jrteo.2022@smu.edu.sg", "test123", "002", 1),
(4, "06581996653", "clarissatoh.2022@smu.edu.sg", "test123", "003", 1),
(5, "06592366941", "benedictlee.2022@smu.edu.sg", "test123", "004", 1),
(6, "00000000001", "teo.junrui.jonathanedward3@gmail.com", "test123", "005", 0),
(7, "00000000002", "teo.junrui.jonathanedward4@gmail.com", "test123", "006", 0),
(8, "00000000003", "teo.junrui.jonathanedward5@gmail.com", "test123", "007", 0),
(9, "00000000004", "kaifengwoo3@gmail.com", "test123", "008", 0),
(10,"00000000005", "jialingng676@gmail.com", "test123", "009", 1),
(11,"00000000006", "fongzhengde03@gmail.com", "test123", "010", 1),
(12,"00000000007", "sharifahbintehairulllizam@gmail.com", "test123", "011", 1),
(13, "00000000008", "amandaxallen5@gmail.com", "test123", "012", 1);

# select * from ticketmaster.event;
# Add events
insert into ticketmaster.event (event_id, event_name, max_queueable, is_highlighted, description, poster_image_path) values
("tswift-era-2024", "Taylor Swift The Eras Tour", 3, 1, 
"The Eras Tour is the ongoing sixth concert tour by American singer-songwriter Taylor Swift, 
who described it as a journey through all of her musical \"eras\". A homage to her albums, 
the Eras Tour is her most expansive tour yet, 
with 146 dates across five continents. It is her second all-stadium tour after the 2018 Reputation Stadium Tour.", # 346 characters
"taylor-swift.png"),
("cplay-mots-2024", "Coldplay: Music of the Spheres Tour", 4, 1,
"Music of the Spheres (subtitled Vol I. From Earth with Love) is the ninth studio album by British rock band Coldplay, 
released on 15 October 2021 by Parlophone in the United Kingdom and Atlantic Records in the United States. It features guest appearances from Selena Gomez, 
We Are King, Jacob Collier and BTS, as well as returning contributions from electronic producer Jon Hopkins.",
"coldplay.jpg"),
("gnr-2023", "Guns N' Roses: The We're F'N' Back! Tour", 2, 0, 
"The We're F'N' Back! Tour is an ongoing concert tour by hard rock band Guns N' Roses.",
"guns_and_roses.jpg" ),
("anson-2023", "Anson Seabra: The Neverland Tour", 2, 0,
"An emotive singer/songwriter, Anson Seabra makes artfully crafted and philosophical, piano-driven pop. 
He emerged online in 2018 and garnered a strong social media fan base with songs like \"Broken\" and \"Emerald Eyes\". 
A Kansas City, Missouri native, Seabra started taking piano lessons at age six and played in school bands growing up. 
After high school, however, he pursued a computer science degree in college while continuing to write and record his own songs.",
"the-neverland-tour.jpg"
),
("twice-rtb-2023", "TWICE: Ready to Be", 3, 1, 
"Twice 5th World Tour \"Ready to Be\" is the third worldwide concert tour 
and the fifth overall concert headlined by South Korean girl group Twice, in support of their twelfth extended play Ready to Be. 
TWICE concert schedule 2023 includes multiple stops throughout the year, commencing with two nights at the KSPO Dome in Seoul on April 15 and 16. 
TWICE will continue their tour in May with shows in Sydney, Melbourne, Osaka, and Tokyo.",
"twice.jpg"),
("greenwich-2023", "Greenwich Comedy Festival", 1, 0,
"London's biggest comedy spectacular Greenwich Comedy Festival this year returns to the National Maritime Museum with stand-up's 
finest live in the Big Top, plus craft beers and street eats and more. This year the festival celebrates its 15th anniversary.
They're joined by the master of dry comedy Stewart Lee, the queen of ventriloquism Nina Conti, plus the likes of Fern Brady, 
Russell Kane, Tim Key, plus Netflix star Phil Wang and many more.",
"greenwich-2023.jpeg"),
("andrea-b-2023", "Andrea Bocelli: Stifel", 5, 1,
"Andrea Bocelli is one of the most recognizable voices in the world. For three decades, his lush arias and stylistic range 
have made him the bestselling classical solo artist in history (selling 90 million albums worldwide). The blind, Tuscany-born tenor's 
concerts are a testament to his range and crossover appeal,
where his 2020 \"Music for Hope\" Easter concert was viewed 28 million times worldwide within the first 24 hours.",
"andrea-bocelli.jpg"),
("enhypen-2023", "Enhypen: Fate", 3, 0,
"Enhypen is a South Korean boy band formed by Belift Lab, a former joint venture between CJ ENM and Hybe Corporation, through the 2020 survival competition show I-Land.
The group is composed of seven members: Heeseung, Jay, Jake, Sunghoon, Sunoo, Jungwon, and Ni-ki. They debuted on November 30, 2020, with the extended play",
"enhypen.jpeg"),
("ed-sheeran-2024", "Ed Sheeran: The Mathematics Tour", 3, 0,
"It's been a quick rise for soulful British singer/songwriter Ed Sheeran. He played his first club gig in New York City in early 2012, 
and by the summer of 2013 tickets for his first headlining concert at New York's Madison Garden sold out in just three minutes. His song \"The A Team\" was nominated for a \"Song of the Year\" Grammy, and his debut album, \"+,\" 
hit No. 5 on the US album chart. Sheeran's second album (2014) features famed producer Rick Rubin behind the board.",
"ed-sheeran-2023.jpg"
),
("pink-sc-2024", "P!INK: Summer Carnival", 2, 0,
"P!NK is one of pop music's most dynamic vocalists and stage performers. 
Since releasing her debut studio album Can't Take Me Home in 2000, the three-time Grammy Award-winning artist (born Alecia Beth Moore) has sold 60 million albums worldwide.
P!NK also cemented her status as a touring powerhouse when she wrapped 2018-19's Beautiful Trauma World Tour.",
"pink-sc-2024.jpg"
);

### Add countries of events
insert into event_countries (event_id, country) VALUES 
("tswift-era-2024", "Mexico"),
("tswift-era-2024", "Argentina"),
("tswift-era-2024", "Brazil"),
("tswift-era-2024", "Japan"),
("tswift-era-2024", "Australia"),
("tswift-era-2024", "Singapore"),
("tswift-era-2024", "France"),
("tswift-era-2024", "Sweden"),
("tswift-era-2024", "Portugal"),
("tswift-era-2024", "Spain"),
("cplay-mots-2024", "Tokyo"),
("cplay-mots-2024", "Kaohsiung City"),
("cplay-mots-2024", "Jakarta"),
("cplay-mots-2024", "Perth"),
("cplay-mots-2024", "Kuala Lumpur"),
("cplay-mots-2024", "Manila"),
("cplay-mots-2024", "Singapore"),
("cplay-mots-2024", "Bangkok"),
("gnr-2023", "North America"),
("gnr-2023", "Europe"),
("gnr-2023", "South America"),
("gnr-2023", "Asia"),
("gnr-2023", "Oceania"),
("anson-2023", "Taipei"),
("anson-2023", "Thailand"),
("anson-2023", "Singapore"),
("anson-2023", "Manila"),
("anson-2023", "Hong Kong"),
("anson-2023", "Paris"),
("twice-rtb-2023", "Asia"),
("twice-rtb-2023", "North America"),
("twice-rtb-2023", "Oceania"),
("twice-rtb-2023", "Europe"),
("twice-rtb-2023", "South America"),
("greenwich-2023", "United Kingdom"),
("andrea-b-2023", "North America"),
("andrea-b-2023", "South America"),
("enhypen-2023", "South Korea"),
("enhypen-2023", "Osaka"),
("enhypen-2023", "Tokyo"),
("enhypen-2023", "California"),
("enhypen-2023", "Texas"),
("enhypen-2023", "New Jersey"),
("enhypen-2023", "Illinois"),
("ed-sheeran-2024", "North America"),
("ed-sheeran-2024", "South America"),
("pink-sc-2024", "United States"),
("pink-sc-2024", "Germany"),
("pink-sc-2024", "Canada"),
("pink-sc-2024", "France"),
("pink-sc-2024", "Poland"),
("pink-sc-2024", "Netherlands"),
("pink-sc-2024", "Astria"),
("pink-sc-2024", "England"),
("pink-sc-2024", "Belgium");

