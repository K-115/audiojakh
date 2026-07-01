-- Safely remove previous reviews and dependent child data
TRUNCATE TABLE reviews CASCADE;

-- Insert new reviews matching the V20 schema format
INSERT INTO reviews (
    content,
    user_id,
    likes,
    date_of_review,
    rating,
    track_spotify_id,
    track_name,
    album_spotify_id,
    album_name,
    artist_name,
    image_url
) VALUES
      (
          'An absolute masterpiece of modern songwriting. The vocal performance is breathtaking.',
          1, 0, NOW(), 4,
          '1WaCWIaxt2mImuXRtjqSg4', 'Halo',
          '39P7VD7qlg3Z0ltq60eHp7', 'I Am... Sasha Fierce',
          'Beyoncé', 'https://i.scdn.co/image/ab67616d0000b273b4c5982e1b92f97a126fc18c'
      ),
      (
          'Ariana Grande does it again, masterpiece, are we surprised?',
          2, 0, NOW(), 5,
          '2QIcCx7MV914oIL2JmBoBV', 'in my head',
          '2fYhqwDWXjbpjaIJPEfKFw', 'thank u, next',
          'Ariana Grande', 'https://i.scdn.co/image/ab67616d0000b27356ac7b86e090f307e218e9c8'
      ),
      (
          'Catchy and fun, best album of all time, best song of all time.',
          3, 0, NOW(), 5,
          '5ChkMS8OtdzJeqyybCc9R5', 'Billie Jean',
          '2ANVost0y2y52ema1E9xAZ', 'Thriller',
          'Michael Jackson', 'https://i.scdn.co/image/ab67616d0000b27332a7d87248d1b75463483df5'
      ),
      (
          'Brilliant pacing and emotional depth. One of the best music pieces available.',
          4, 0, NOW(), 5,
          '2MvIMgtWyK88OiPi0J8Dg3', 'Psychosocial',
          '0hFWapnP7orzXCMwNU5DuA', 'All Hope Is Gone',
          'Slipknot', 'https://i.scdn.co/image/ab67616d0000b273e7fbc0883149094912559f2c'
      ),
      (
          'An absolute masterpiece of modern songwriting. The vocal performance is breathtaking.',
          1, 0, NOW(), 3,
          '3weNRklVDqb4Rr5MhKBR3D', 'Nuvole Bianche',
          '0Ryad9M1b1MxSjgXdHCh1c', 'Una Mattina',
          'Ludovico Einaudi', 'https://i.scdn.co/image/ab67616d0000b2734ea8c5801bfdf5ac90719400'
      ),
      (
          'Beat is crazy, open up the door.',
          2, 0, NOW(), 5,
          '7BRD7x5pt8Lqa1eGYC4dzj', 'CHIHIRO',
          '7aJuG4TFXa2hmE4z1yxc3n', 'HIT ME HARD AND SOFT',
          'Billie Eilish', 'https://i.scdn.co/image/ab67616d0000b27371d62ea7ea8a5be92d3c1f62'
      ),
      (
          'I did not like it, it feels a bit dated compared to their newer production styles.',
          3, 0, NOW(), 1,
          '5KqldkCunQ2rWxruMEtGh0', 'The Gambler',
          '3XdvQjIMjdwxRivZrg1ziJ', 'The Gambler',
          'Kenny Rogers', 'https://i.scdn.co/image/ab67616d0000b273c289ed4316aaab5a5b7376c1'
      ),
      (
          'Brilliant pacing and emotional depth. One of the best music pieces available.',
          4, 0, NOW(), 4,
          '7MJQ9Nfxzh8LPZ9e9u68Fq', 'Lose Yourself',
          '353HFOqGHySp027oyr3aGs', '8 Mile (Music from and Inspired by the Motion Picture) ',
          'Eminem', 'https://i.scdn.co/image/ab67616d0000b273eab40fc794b88b9d1e012578'
      );
