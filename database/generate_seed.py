"""
Generates rich seed data for HMS spanning Jan-Jun 2026 (LKR).
Output: overwrites seed.sql
"""

import random, sys
from datetime import date, timedelta

random.seed(42)

def LKR(v):
    return f"{v:.2f}"

def sql_date(d):
    return d.strftime("%Y-%m-%d")

def sql_ts(d, h=12, m=0):
    return d.strftime(f"%Y-%m-%d {h:02d}:{m:02d}:00")

def nights(cin, cout):
    return (cout - cin).days

def did_str(d, seq):
    return f"RES-{d.strftime('%Y%m%d')}-{seq:05d}"

TAX = 0.10
LATE_FEE = 5000.0

# ── Data ──────────────────────────────────────────────────────────

guests = [
    (1,"Ranil","Wickramasinghe","ranil.w@email.com","0771001001",
     "15 Marine Drive, Colombo 03","Passport","P1001001","1970-04-12","VIP","Sri Lankan"),
    (2,"Sarah","Thompson","sarah.t@email.com","0771002002",
     "42 Hyde Park Corner, London","Passport","P2002002","1982-09-25","VIP","British"),
    (3,"Ahmed","Al-Rashid","ahmed.ar@email.com","0771003003",
     "Palm Jumeirah, Dubai","Passport","P3003003","1975-11-18","VIP","Emirati"),
    (4,"Kenji","Tanaka","kenji.t@email.com","0771004004",
     "5-2-1 Ginza, Tokyo","Passport","P4004004","1980-06-30","VIP","Japanese"),
    (5,"Maria","Santos","maria.s@email.com","0771005005",
     "25 Ayala Avenue, Manila","Passport","P5005005","1985-03-08","VIP","Filipino"),
    (6,"Priya","Sharma","priya.s@email.com","0772001001",
     "44 Tech Park, Bangalore","Passport","P6006006","1988-07-15","Corporate","Indian"),
    (7,"David","Miller","david.m@email.com","0772002002",
     "88 Queen Street, Sydney","Passport","P7007007","1979-12-22","Corporate","Australian"),
    (8,"Liu","Wei","liu.w@email.com","0772003003",
     "100 Century Ave, Shanghai","Passport","P8008008","1983-05-10","Corporate","Chinese"),
    (9,"Samantha","Perera","samantha.p@email.com","0772004004",
     "66 Union Place, Colombo 02","Driving License","DL909090","1990-01-28","Corporate","Sri Lankan"),
    (10,"Robert","Fischer","robert.f@email.com","0772005005",
     "12 Hauptstrasse, Berlin","Passport","P0000009","1986-08-14","Corporate","German"),
    (11,"Nimal","Fernando","nimal.f@email.com","0773001001",
     "23 Temple Road, Kandy","Driving License","DL111111","1987-04-05","Regular","Sri Lankan"),
    (12,"Kamala","Dissanayake","kamala.d@email.com","0773002002",
     "56 Park Street, Colombo 05","Driving License","DL222222","1992-10-19","Regular","Sri Lankan"),
    (13,"Mark","Johnson","mark.j@email.com","0773003003",
     "34 Abbey Road, London","Passport","P1313131","1991-03-22","Regular","British"),
    (14,"Elena","Petrova","elena.p@email.com","0773004004",
     "10 Tverskaya Street, Moscow","Passport","P1414141","1994-07-08","Regular","Russian"),
    (15,"Ahmed","Hassan","ahmed.h@email.com","0773005005",
     "7 Orchid Magu, Male","Passport","P1515151","1983-11-30","Regular","Maldivian"),
    (16,"Lisa","Chen","lisa.c@email.com","0773006006",
     "28 Orchard Road, Singapore","Passport","P1616161","1990-02-14","Regular","Singaporean"),
    (17,"Sunil","Rajapaksa","sunil.r@email.com","0773007007",
     "89 Galle Road, Matara","Driving License","DL171717","1985-09-10","Regular","Sri Lankan"),
    (18,"Marie","Dubois","marie.d@email.com","0773008008",
     "55 Champs-Elysees, Paris","Passport","P1818181","1989-06-17","Regular","French"),
    (19,"Amara","Silva","amara.s@email.com","0773009009",
     "12 Lotus Road, Colombo 01","Driving License","DL191919","1993-12-25","Regular","Sri Lankan"),
    (20,"Peter","ONeill","peter.o@email.com","0773010010",
     "66 Temple Bar, Dublin","Passport","P2020202","1981-05-05","Regular","Irish"),
    (21,"Fatima","AlZahra","fatima.z@email.com","0773011011",
     "15 Corniche Road, Abu Dhabi","Passport","P2121212","1988-08-20","Regular","Emirati"),
    (22,"Yuki","Nakamura","yuki.n@email.com","0773012012",
     "8 Shibuya Crossing, Tokyo","Passport","P2222222","1995-01-12","Regular","Japanese"),
    (23,"Chaminda","Bandara","chaminda.b@email.com","0774001001",
     "34 High Level Road, Nugegoda","Driving License","DL232323","1996-04-03","Regular","Sri Lankan"),
    (24,"Ravi","Kumar","ravi.k@email.com","0774002002",
     "22 MG Road, Mumbai","Passport","P2424242","1994-09-15","Regular","Indian"),
    (25,"Olga","Ivanova","olga.i@email.com","0774003003",
     "5 Nevsky Prospect, St Petersburg","Passport","P2525252","1997-02-28","Regular","Russian"),
    (26,"Thanuja","Weerasinghe","thanuja.w@email.com","0774004004",
     "78 Stanley Road, Jaffna","Driving License","DL262626","1991-07-22","Regular","Sri Lankan"),
    (27,"Mohamed","Rizwan","mohamed.r@email.com","0774005005",
     "3 Majeedhee Magu, Male","Passport","P2727272","1993-10-10","Regular","Maldivian"),
    (28,"Charlotte","Williams","charlotte.w@email.com","0775001001",
     "10 Downing Street, London","Passport","P2828282","1998-06-01","Regular","British"),
    (29,"Dinesh","Jayasuriya","dinesh.j@email.com","0775002002",
     "45 Kandy Road, Kurunegala","Driving License","DL292929","1989-03-15","Regular","Sri Lankan"),
    (30,"Ahmed","Naseem","ahmed.n@email.com","0775003003",
     "12 Fareedhee Magu, Male","Passport","P3030303","1992-11-20","Regular","Maldivian"),
]

rooms = [
    ("101","Single",1,12000,"Compact single with work desk","available",1),
    ("102","Single",1,13000,"Standard single with garden view","available",1),
    ("103","Single",1,14000,"Single room with natural light","maintenance",1),
    ("104","Single",1,12000,"Budget-friendly single room","available",1),
    ("105","Single",1,15000,"Single room with city view","available",1),
    ("106","Single",1,18000,"Premium single with balcony","occupied",1),
    ("201","Double",2,18000,"Standard double room","available",2),
    ("202","Double",2,20000,"Double room with work area","occupied",2),
    ("203","Double",2,21000,"Double room with seating area","available",2),
    ("204","Double",2,22000,"Spacious double room","occupied",2),
    ("205","Double",2,24000,"Double with city skyline view","reserved",2),
    ("206","Double",2,25000,"Premium double extra amenities","available",2),
    ("207","Double",2,25000,"Corner double dual windows","available",2),
    ("208","Double",2,22000,"Double room with sofa seating","occupied",2),
    ("301","Suite",3,35000,"Junior suite with living area","available",3),
    ("302","Suite",3,38000,"Executive suite work desk","occupied",3),
    ("303","Suite",3,40000,"Family suite extra seating","available",3),
    ("304","Suite",4,45000,"Large family suite","occupied",3),
    ("305","Suite",4,48000,"Premium suite panoramic view","reserved",3),
    ("306","Suite",4,55000,"Presidential suite","available",3),
    ("401","Deluxe",3,25000,"Deluxe room modern finish","occupied",4),
    ("402","Deluxe",3,28000,"Deluxe room premium bedding","available",4),
    ("501","Deluxe",4,30000,"Spacious deluxe extra bed","available",5),
    ("502","Deluxe",4,35000,"Penthouse deluxe suite","reserved",5),
]

staff = [
    ("Alice","Johnson","alice.johnson@hms.com","0779988776","Manager","Administration",350000,"2023-01-15","active"),
    ("Bob","Williams","bob.williams@hms.com","0778877665","Receptionist","Front Desk",95000,"2023-03-01","active"),
    ("Carol","Davis","carol.davis@hms.com","0777766554","Receptionist","Front Desk",85000,"2023-06-12","active"),
    ("Daniel","Brown","daniel.brown@hms.com","0776655443","Housekeeper","Housekeeping",75000,"2024-02-20","active"),
    ("Eva","Martinez","eva.martinez@hms.com","0775544332","Housekeeper","Housekeeping",65000,"2024-04-10","on_leave"),
    ("Frank","Wilson","frank.wilson@hms.com","0774433221","Maintenance","Maintenance",80000,"2023-09-05","active"),
    ("Gayan","Fernando","gayan.fernando@hms.com","0773322110","Concierge","Front Desk",100000,"2024-08-01","active"),
    ("Priyanka","Jayawardena","priyanka.j@hms.com","0772211009","Shift Supervisor","Administration",150000,"2024-01-10","active"),
]

services = [
    ("Breakfast","Food",1500,"Continental breakfast 7-10 AM","TRUE"),
    ("Lunch","Food",2500,"Three-course lunch 12-2 PM","TRUE"),
    ("Dinner","Food",3500,"Four-course dinner 7-10 PM","TRUE"),
    ("Laundry Wash","Laundry",800,"Standard wash and fold","TRUE"),
    ("Laundry Dry Clean","Laundry",1500,"Professional dry cleaning","TRUE"),
    ("Spa Massage","Spa",7500,"Full body massage 60 min","TRUE"),
    ("Spa Sauna","Spa",4500,"Sauna access per session","TRUE"),
    ("Conference Room","Conference",25000,"Conference room per hour","FALSE"),
    ("Airport Transfer","Transport",5000,"One-way airport transfer","TRUE"),
    ("Mini Bar","Beverage",2500,"In-room mini bar charges","TRUE"),
]

# ── Reservations builder ──────────────────────────────────────────

reservations = []  # (guest_id, room_idx_1, cin_str, cout_str, guests, status, display_id, total, notes)
PMT_METHODS = ["Credit Card","Credit Card","Credit Card","Credit Card","Credit Card",
               "Cash","Cash","Cash","Cash","Cash","Cash",
               "Bank Transfer","Bank Transfer","Bank Transfer","Bank Transfer",
               "Mobile Payment"]

# These will hold service assignments per reservation
# key = res_index, value = [(service_id_1based, qty)]

res_services = {}

def R(gid, ridx, cin, cout, g, status, notes=None):
    """Add a reservation and compute total"""
    n = nights(cin, cout)
    price = rooms[ridx-1][3]
    base = price * n
    did = did_str(cin, len(reservations)+1)
    reservations.append((gid, ridx, sql_date(cin), sql_date(cout), g, status, did, base, notes))
    return len(reservations)-1  # return index

def assign_svc(res_idx, svc_items):
    """svc_items: [(svc_id_1based, qty), ...]"""
    res_services[res_idx] = svc_items

# ── JANUARY (high season) ─────────────────────────────────────────
# Guest 1 (VIP Ranil): 3 stays across months
idx = R(1, 16, date(2026,1,2), date(2026,1,6), 2, "checked_out")  # Room 302
assign_svc(idx, [(6,1),(7,1),(3,2),(9,1)])  # spa massage, sauna, dinner x2, airport transfer
idx = R(1, 22, date(2026,3,15), date(2026,3,17), 2, "checked_out")  # Room 402
assign_svc(idx, [(6,1),(1,2)])
idx = R(1, 16, date(2026,6,1), date(2026,6,5), 2, "checked_out")  # Room 302
assign_svc(idx, [(6,1),(3,3),(9,1)])

# Guest 3 (VIP Ahmed): 3 stays
idx = R(3, 7, date(2026,1,5), date(2026,1,8), 2, "checked_out")  # Room 201
assign_svc(idx, [(7,2),(3,1)])
idx = R(3, 14, date(2026,4,1), date(2026,4,4), 2, "checked_out")  # Room 208
assign_svc(idx, [(6,1),(2,2)])
idx = R(3, 12, date(2026,6,6), date(2026,6,8), 2, "checked_out")  # Room 206
assign_svc(idx, [(6,1),(7,1)])

# Guest 2 (VIP Sarah)
idx = R(2, 21, date(2026,1,8), date(2026,1,12), 2, "checked_out")  # Room 401
assign_svc(idx, [(6,2),(7,2),(3,4),(9,1)])

# Guest 4 (VIP Kenji)
idx = R(4, 6, date(2026,1,16), date(2026,1,19), 1, "checked_out")  # Room 106
assign_svc(idx, [(7,1),(3,1)])

# Guest 5 (VIP Maria)
idx = R(5, 18, date(2026,1,22), date(2026,1,26), 3, "checked_out")  # Room 304
assign_svc(idx, [(6,1),(3,2),(4,1),(10,1)])

# Guest 6 (Corporate Priya)
idx = R(6, 10, date(2026,1,18), date(2026,1,21), 2, "checked_out")  # Room 204
assign_svc(idx, [(2,1),(8,2)])  # lunch, conference room

# Guest 7 (Corporate David)
idx = R(7, 8, date(2026,1,10), date(2026,1,13), 2, "checked_out")  # Room 202
assign_svc(idx, [(3,1),(8,1)])

# Guest 8 (Corporate Liu Wei)
idx = R(8, 13, date(2026,1,12), date(2026,1,15), 2, "checked_out")  # Room 207
assign_svc(idx, [(1,3),(3,1)])

# Guest 9 (Corporate Samantha)
idx = R(9, 18, date(2026,1,14), date(2026,1,16), 2, "checked_out")  # Room 304
assign_svc(idx, [(8,1)])

# Guest 10 (Corporate Robert)
idx = R(10, 19, date(2026,1,6), date(2026,1,10), 2, "checked_out")  # Room 305

# Guest 11 (Regular Nimal) — 3 stays
idx = R(11, 14, date(2026,1,20), date(2026,1,24), 3, "checked_out")  # Room 208
assign_svc(idx, [(1,4),(4,2)])

# Jan cancellations
R(12, 17, date(2026,1,24), date(2026,1,27), 3, "cancelled", "Family emergency, cancelled night before")
R(13, 12, date(2026,1,15), date(2026,1,18), 2, "cancelled", "Flight cancelled due to weather")

# ── FEBRUARY ──────────────────────────────────────────────────────
idx = R(14, 6, date(2026,2,1), date(2026,2,4), 1, "checked_out")  # Room 106
assign_svc(idx, [(1,3)])

idx = R(15, 17, date(2026,2,3), date(2026,2,7), 2, "checked_out")  # Room 303
assign_svc(idx, [(1,5),(4,1)])

idx = R(16, 7, date(2026,2,5), date(2026,2,8), 1, "checked_out")
assign_svc(idx, [(1,2)])

idx = R(17, 20, date(2026,2,8), date(2026,2,12), 4, "checked_out")  # Room 306
assign_svc(idx, [(1,4),(4,2)])

idx = R(18, 22, date(2026,2,12), date(2026,2,15), 2, "checked_out")
assign_svc(idx, [(3,3),(5,1)])

idx = R(19, 8, date(2026,2,14), date(2026,2,16), 1, "checked_out")
assign_svc(idx, [(10,1)])

idx = R(20, 13, date(2026,2,18), date(2026,2,21), 2, "checked_out")
assign_svc(idx, [(3,1)])

idx = R(21, 23, date(2026,2,20), date(2026,2,24), 3, "checked_out")  # Room 501
assign_svc(idx, [(1,4),(10,2)])

idx = R(22, 12, date(2026,2,22), date(2026,2,25), 2, "checked_out")  # Room 206

idx = R(6, 9, date(2026,2,25), date(2026,2,28), 2, "checked_out")  # Room 203
assign_svc(idx, [(2,1),(8,1)])

R(23, 4, date(2026,2,14), date(2026,2,16), 1, "cancelled", "Guest fell ill, rescheduled")

# ── MARCH ──────────────────────────────────────────────────────────
idx = R(24, 1, date(2026,3,1), date(2026,3,3), 1, "checked_out")
idx = R(25, 2, date(2026,3,4), date(2026,3,6), 1, "checked_out")
idx = R(26, 3, date(2026,3,7), date(2026,3,9), 1, "checked_out")
idx = R(27, 5, date(2026,3,10), date(2026,3,12), 1, "checked_out")

# Guest 7 (Corporate David) — 2nd stay
idx = R(7, 9, date(2026,3,5), date(2026,3,9), 2, "checked_out")
assign_svc(idx, [(3,3),(8,1)])

# Guest 2 (VIP Sarah) — 2nd stay
idx = R(2, 15, date(2026,3,12), date(2026,3,16), 2, "checked_out")  # Room 301
assign_svc(idx, [(6,1),(3,4),(9,1)])

# Guest 5 (VIP Maria) — 2nd stay
idx = R(5, 19, date(2026,3,18), date(2026,3,22), 3, "checked_out")  # Room 305
assign_svc(idx, [(6,1),(7,1),(2,2),(10,1)])

# Guest 8 (Liu Wei) — 2nd stay
idx = R(8, 8, date(2026,3,20), date(2026,3,23), 2, "checked_out")
assign_svc(idx, [(1,3)])

# Guest 10 (Robert) — 2nd stay
idx = R(10, 10, date(2026,3,22), date(2026,3,25), 2, "checked_out")

idx = R(14, 4, date(2026,3,25), date(2026,3,28), 1, "checked_out")
idx = R(16, 13, date(2026,3,28), date(2026,3,30), 1, "checked_out")

R(18, 23, date(2026,3,5), date(2026,3,8), 2, "cancelled", "Travel advisory issued")
R(20, 20, date(2026,3,15), date(2026,3,18), 2, "cancelled", "Found alternative hotel")

# ── APRIL ──────────────────────────────────────────────────────────
# Guest 9 (Samantha) — 2nd stay
idx = R(9, 7, date(2026,5,10), date(2026,5,12), 2, "checked_out")  # Room 201

# Guest 11 (Nimal) — 2nd stay
idx = R(11, 15, date(2026,4,20), date(2026,4,23), 3, "checked_out")  # Room 301
assign_svc(idx, [(1,3),(4,2),(10,1)])

idx = R(12, 16, date(2026,4,2), date(2026,4,5), 3, "checked_out")  # Room 302
assign_svc(idx, [(1,3),(10,1)])

idx = R(15, 18, date(2026,4,5), date(2026,4,9), 2, "checked_out")  # Room 304
assign_svc(idx, [(3,2),(5,1)])

idx = R(17, 21, date(2026,4,8), date(2026,4,12), 4, "checked_out")  # Room 401
assign_svc(idx, [(1,4),(4,2),(10,2)])

idx = R(19, 9, date(2026,4,12), date(2026,4,15), 1, "checked_out")  # Room 203
idx = R(21, 24, date(2026,4,15), date(2026,4,18), 3, "checked_out")  # Room 502
assign_svc(idx, [(3,3),(7,2)])

idx = R(22, 7, date(2026,4,18), date(2026,4,20), 2, "checked_out")
idx = R(6, 12, date(2026,4,22), date(2026,4,25), 2, "checked_out")
assign_svc(idx, [(2,2)])

idx = R(7, 14, date(2026,4,25), date(2026,4,28), 2, "checked_out")

R(13, 18, date(2026,4,10), date(2026,4,13), 2, "cancelled", "Duplicate booking by agent")

# ── MAY (low season) ──────────────────────────────────────────────
idx = R(23, 2, date(2026,5,1), date(2026,5,3), 1, "checked_out")
idx = R(24, 1, date(2026,5,4), date(2026,5,5), 1, "checked_out")
idx = R(25, 4, date(2026,5,7), date(2026,5,9), 1, "checked_out")
idx = R(26, 5, date(2026,5,12), date(2026,5,14), 1, "checked_out")
idx = R(27, 2, date(2026,5,16), date(2026,5,18), 1, "checked_out")
idx = R(14, 1, date(2026,5,20), date(2026,5,22), 1, "checked_out")
idx = R(16, 4, date(2026,5,24), date(2026,5,26), 1, "checked_out")
idx = R(19, 5, date(2026,5,28), date(2026,5,30), 1, "checked_out")

R(24, 3, date(2026,5,10), date(2026,5,12), 1, "cancelled", "Visa not approved")
R(25, 3, date(2026,5,22), date(2026,5,25), 1, "cancelled", "Personal reasons, may rebook")

# ── JUNE (recovering + current + future) ──────────────────────────
# Guest 11 (Nimal) — 3rd stay (current)
idx = R(11, 9, date(2026,6,20), date(2026,6,23), 3, "checked_in")

# Guest 4 (Kenji VIP) — 2nd stay (current)
idx = R(4, 6, date(2026,6,24), date(2026,6,28), 1, "checked_in")

# Guest 2 (Sarah VIP) — 3rd stay (current)
idx = R(2, 22, date(2026,6,23), date(2026,6,27), 2, "checked_in")
assign_svc(idx, [(6,1),(3,2),(9,1)])

# Guest 8 (Liu Wei) — 3rd stay (current)
idx = R(8, 13, date(2026,6,25), date(2026,6,29), 2, "checked_in")

# Guest 10 (Robert) — 3rd stay (current)
idx = R(10, 20, date(2026,6,26), date(2026,6,30), 2, "checked_in")  # Room 306
assign_svc(idx, [(3,2),(8,1)])

# Guest 5 (VIP Maria) — 3rd stay (checked_out early Jun)
idx = R(5, 18, date(2026,6,8), date(2026,6,12), 3, "checked_out")  # Room 304
assign_svc(idx, [(6,1),(3,2),(5,1)])

# Guest 7 (Corporate David) — already had 2 stays, another
idx = R(7, 8, date(2026,6,12), date(2026,6,15), 2, "checked_out")  # Room 202
assign_svc(idx, [(2,1)])

# Guest 9 (Samantha) checked_out Jun
idx = R(9, 9, date(2026,6,15), date(2026,6,17), 2, "checked_out")  # Room 203

# Confirmed (upcoming)
R(12, 15, date(2026,6,29), date(2026,7,2), 3, "confirmed", "Prefer top floor")
R(15, 17, date(2026,6,30), date(2026,7,3), 2, "confirmed", "Honeymoon couple - anniversary package")

# Pending
R(17, 12, date(2026,7,5), date(2026,7,8), 3, "pending", "Requesting early check-in")
R(19, 10, date(2026,7,8), date(2026,7,11), 1, "pending", "Prefers quiet room")

# Jun cancellations
R(22, 15, date(2026,6,12), date(2026,6,14), 2, "cancelled", "Booking conflict with other hotel")
R(26, 6, date(2026,6,18), date(2026,6,20), 1, "cancelled", "Family emergency - full refund requested")

R(3, 11, date(2026,6,6), date(2026,6,8), 2, "checked_out")  # Room 205

print(f"Total reservations: {len(reservations)}", file=sys.stderr)

# ── Billing ──────────────────────────────────────────────────────
billing = []  # (res_index, ...)
bid = 0
_pmt_idx = 0

for i, r in enumerate(reservations):
    if r[5] != "checked_out":
        continue
    gid, ridx, cin_s, cout_s, g, stat, did, base, notes = r
    cin = date.fromisoformat(cin_s)
    cout = date.fromisoformat(cout_s)
    n = nights(cin, cout)
    price = rooms[ridx-1][3]
    rc = price * n

    # service charge from assigned services
    svc_items = res_services.get(i, [])
    svc_total = sum(services[sid-1][2] * qty for sid, qty in svc_items)

    late = LATE_FEE if random.random() < 0.15 else 0.0
    other = 500.0 if late > 0 else 0.0
    discount = round(rc * 0.10, 2) if 6 <= gid <= 10 else 0.0

    taxable = rc + svc_total + late + other - discount
    tax = round(taxable * TAX, 2)
    total = round(taxable + tax, 2)

    # payment
    roll = random.random()
    if roll < 0.80:
        pstat = "paid"; amt = total
    elif roll < 0.95:
        pstat = "partial"; amt = round(total * random.uniform(0.3, 0.7), 2)
    else:
        pstat = "pending"; amt = 0.0

    pmt = PMT_METHODS[_pmt_idx % len(PMT_METHODS)]
    _pmt_idx += 1
    txn = f"TXN{1000+bid:04d}"

    pd = sql_ts(cout, random.randint(8,18), random.randint(0,59)) if pstat != "pending" else None
    pdate = f"'{pd}'" if pd else "NULL"
    pnote = {"paid":"Full payment at checkout",
             "partial":f"Partial payment of {LKR(amt)} received",
             "pending":"Payment pending"}[pstat]
    bnote = f"{n} night(s) in Room {rooms[ridx-1][0]}"
    if svc_total > 0: bnote += f", Services: {LKR(svc_total)}"
    if discount > 0: bnote += ", Corp discount 10%"
    if late > 0: bnote += f", Late charge: {LKR(late)}"

    billing.append((i, did, rc, svc_total, other, discount, late, tax, total, pstat, amt, pmt, txn, pnote, pdate, bnote))

# Add 3 refunds for cancelled
refund_count = 0
for i, r in enumerate(reservations):
    if r[5] == "cancelled" and refund_count < 3:
        gid, ridx, cin_s, cout_s, g, stat, did, base, notes = r
        cin = date.fromisoformat(cin_s)
        cout = date.fromisoformat(cout_s)
        n = nights(cin, cout)
        price = rooms[ridx-1][3]
        rc = price * n
        tax = round(rc * TAX, 2)
        total = round(rc + tax, 2)
        refund_date = sql_ts(cout, 10, 0)
        billing.append((i, did, rc, 0, 0, 0, 0, tax, total, "refunded", total,
                        "Credit Card", f"TXN{9000+refund_count:04d}",
                        "Full refund processed after cancellation",
                        f"'{refund_date}'",
                        f"Cancelled - refund issued for Room {rooms[ridx-1][0]}"))
        refund_count += 1

print(f"Total billing: {len(billing)}", file=sys.stderr)

# ── Service Bookings ─────────────────────────────────────────────
svc_bookings = []
for res_idx, svc_items in res_services.items():
    r = reservations[res_idx]
    if r[5] == "cancelled":
        svc_status = "cancelled"
    elif r[5] in ("checked_out", "checked_in"):
        svc_status = "completed"
    else:
        svc_status = "pending"
    for sid, qty in svc_items:
        price = services[sid-1][2]
        total = price * qty
        svc_bookings.append((res_idx, sid, qty, total, svc_status))

print(f"Total service bookings: {len(svc_bookings)}", file=sys.stderr)

# ── Room Assignments ──────────────────────────────────────────────
assignments = []
for i, r in enumerate(reservations):
    if r[5] != "checked_out":
        continue
    ridx = r[1]
    cout = date.fromisoformat(r[3])
    skey = 4 if random.random() < 0.6 else 5  # Daniel or Eva (idx 4 or 5)
    status = "completed" if cout < date(2026,6,20) else "in_progress"
    assignments.append((ridx, skey, sql_date(cout), "Cleaning", status,
                        "Post-stay cleaning"))

# Maintenance assignments
maint = [(3,"2026-01-10"),(7,"2026-02-15"),(11,"2026-03-20"),
         (15,"2026-04-10"),(20,"2026-05-15"),(24,"2026-06-10")]
for ridx, dt in maint:
    assignments.append((ridx, 6, dt, "Maintenance", "completed", "AC servicing & plumbing"))

# Inspections
ins = [(1,"2026-01-15"),(6,"2026-02-20"),(10,"2026-03-25"),
        (15,"2026-04-15"),(20,"2026-05-20"),(24,"2026-06-15")]
for ridx, dt in ins:
    assignments.append((ridx, 8, dt, "Inspection", "completed", "Monthly quality inspection"))

print(f"Total assignments: {len(assignments)}", file=sys.stderr)

# ══════════════════════════════════════════════════════════════════
# RENDER SQL
# ══════════════════════════════════════════════════════════════════

def esc(s):
    return s.replace("'", "''")

lines = []
def out(s=""):
    lines.append(s)

out("USE hotel_management_system;")
out("")
out("-- ================================================")
out("-- GENERATED RICH SEED DATA  (LKR)  Jan-Jun 2026")
out("-- ================================================")
out("")
out("SET FOREIGN_KEY_CHECKS = 0;")
out("")
for t in ["room_assignments","service_bookings","billing","reservations",
          "services","staff","rooms","guests"]:
    out(f"TRUNCATE TABLE {t};")
out("")
out("SET FOREIGN_KEY_CHECKS = 1;")
out("")

# 1. Guests
out("-- 1. guests (30)")
out("INSERT INTO guests (first_name, last_name, email, phone, address, id_proof_type, id_proof_number, date_of_birth, guest_type, nationality) VALUES")
gvals = []
for g in guests:
    _,fn,ln,em,ph,ad,idtyp,idnum,dob,gt,nat = g
    idtyp_s = f"'{esc(idtyp)}'" if idtyp else "NULL"
    idnum_s = f"'{esc(idnum)}'" if idnum else "NULL"
    gvals.append(f"('{esc(fn)}','{esc(ln)}','{em}','{ph}','{esc(ad)}',{idtyp_s},{idnum_s},'{dob}','{gt}','{nat}')")
out(",\n".join(gvals) + ";")
out()

# 2. Rooms
out("-- 2. rooms (24)")
out("INSERT INTO rooms (room_number, room_type, capacity, base_price, description, status, floor) VALUES")
rvals = []
for r in rooms:
    rn,rt,cp,pr,desc,st,fl = r
    rvals.append(f"('{rn}','{rt}',{cp},{LKR(pr)},'{esc(desc)}','{st}',{fl})")
out(",\n".join(rvals) + ";")
out()

# 3. Staff
out("-- 3. staff (8)")
out("INSERT INTO staff (first_name, last_name, email, phone, position, department, salary, joining_date, status) VALUES")
svals = []
for s in staff:
    fn,ln,em,ph,pos,dep,sal,jd,st = s
    svals.append(f"('{esc(fn)}','{esc(ln)}','{em}','{ph}','{pos}','{dep}',{LKR(sal)},'{jd}','{st}')")
out(",\n".join(svals) + ";")
out()

# 4. Services
out("-- 4. services (10)")
out("INSERT INTO services (service_name, service_type, price, description, is_available) VALUES")
svcvals = []
for s in services:
    nm,styp,pr,desc,av = s
    svcvals.append(f"('{esc(nm)}','{styp}',{LKR(pr)},'{esc(desc)}',{av})")
out(",\n".join(svcvals) + ";")
out()

# 5. Reservations
out("-- 5. reservations (" + str(len(reservations)) + ")")
out("INSERT INTO reservations (guest_id, room_id, check_in_date, check_out_date, number_of_guests, status, total_amount, display_id, notes) VALUES")
rvals = []
for r in reservations:
    gid,rid,cin_s,cout_s,g,stat,did,amt,nt = r
    nt_s = f"'{esc(nt)}'" if nt else "NULL"
    rvals.append(f"({gid},{rid},'{cin_s}','{cout_s}',{g},'{stat}',{LKR(amt)},'{did}',{nt_s})")
out(",\n".join(rvals) + ";")
out()

# 6. Billing
out("-- 6. billing (" + str(len(billing)) + ")")
out("INSERT INTO billing (reservation_id, room_charge, service_charge, other_charges, discount_amount, late_charge, tax_amount, total_bill, payment_status, amount_paid, payment_method, transaction_id, payment_notes, payment_date, notes) VALUES")
bvals = []
for b in billing:
    res_idx,_,rc,svc,oth,disc,late,tax,tot,pstat,amt,pmt,txn,pnote,pdate,bnote = b
    rid = res_idx + 1
    bvals.append(f"({rid},{LKR(rc)},{LKR(svc)},{LKR(oth)},{LKR(disc)},{LKR(late)},{LKR(tax)},{LKR(tot)},'{pstat}',{LKR(amt)},'{esc(pmt)}','{txn}','{esc(pnote)}',{pdate},'{esc(bnote)}')")
out(",\n".join(bvals) + ";")
out()

# 7. Service Bookings
out("-- 7. service_bookings (" + str(len(svc_bookings)) + ")")
out("INSERT INTO service_bookings (reservation_id, service_id, quantity, total_price, status) VALUES")
svals = []
for sb in svc_bookings:
    res_idx, sid, qty, tprice, sstat = sb
    rid = res_idx + 1
    svals.append(f"({rid},{sid},{qty},{LKR(tprice)},'{sstat}')")
out(",\n".join(svals) + ";")
out()

# 8. Room Assignments
out("-- 8. room_assignments (" + str(len(assignments)) + ")")
out("INSERT INTO room_assignments (room_id, staff_id, assignment_date, assignment_type, status, notes) VALUES")
avals = []
for a in assignments:
    rid, sid, dt, atype, astat, anote = a
    avals.append(f"({rid},{sid},'{dt}','{atype}','{astat}','{esc(anote)}')")
out(",\n".join(avals) + ";")
out()
out("-- ================================================")
out("-- END OF SEED DATA")
out("-- ================================================")

# Write
import os
script_dir = os.path.dirname(os.path.abspath(__file__))
output_path = os.path.join(script_dir, "seed.sql")
with open(output_path, "w", encoding="utf-8") as f:
    f.write("\n".join(lines))
print(f"Done. Wrote {output_path}")
