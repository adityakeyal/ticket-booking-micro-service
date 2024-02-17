<div class="mermaid">  
erDiagram

    BOOKING {
        uuid id
        uuid show_id
        uuid user_id
        date show_date
        varchar payment_status
        uuid payment_id
        varchar status
    }
    BOOKING_DETAILS {
        uuid id
        uuid booking_id
        uuid seat_id

    }
     SEAT_INVENTORY {
        uuid id
        uuid show_id
        real price
        int row
        varchar seat
        varchar availability 
    }
    
    BOOKING ||--o{ BOOKING_DETAILS : contains
    SEAT_INVENTORY ||--|| BOOKING_DETAILS: mapped

</div>