<div class="mermaid">  
erDiagram
    MOVIE {
        uuid id
        varchar name
        varchar language
        varchar genre
        varchar release_date
        int running_time_min

    }
    MOVIE_CAST_CREW {
        uuid id
        uuid movie_id
        varchar name
        varchar imageloc
        varchar role 
    }
    MOVIE_IMAGES {
        uuid id
        uuid movie_id
        varchar image_location
    }
    MOVIE_VIDEOS {
        uuid id
        uuid movie_id
        varchar video_location
    }
    MOVIE }o--o{ MOVIE_CAST_CREW : involved
    MOVIE ||--o{ MOVIE_IMAGES : has 
    MOVIE ||--o{ MOVIE_VIDEOS : has
</div>