USE arthistorydb;

DROP PROCEDURE IF EXISTS track_artist;
DELIMITER //
CREATE PROCEDURE track_artist(aname VARCHAR(50))
BEGIN SELECT *
	FROM art_piece
    WHERE aname = artist;
END//

DROP PROCEDURE IF EXISTS track_period//

CREATE PROCEDURE track_period(p VARCHAR(50))
BEGIN SELECT piece_name, artist
	FROM art_piece JOIN artist
    WHERE p = period
    GROUP BY artist, piece_name;
END//

DROP PROCEDURE IF EXISTS track_exhibit//
CREATE PROCEDURE track_exhibit(exhibit_name VARCHAR(50))
BEGIN SELECT *
	FROM art_piece
    WHERE exhibit = exhibit_name;
END //

DROP PROCEDURE IF EXISTS update_artist_period//
CREATE PROCEDURE update_artist_period(artist VARCHAR(30), new_param VARCHAR(20))
BEGIN
	UPDATE artist SET artist.period = new_param WHERE artist = aname;
END//


DROP PROCEDURE IF EXISTS update_artist_year//
CREATE PROCEDURE update_artist_year(artist VARCHAR(30), new_param INT)
BEGIN
	UPDATE artist SET artist.yod = new_param WHERE artist = aname;
END//