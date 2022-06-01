create table if not exists CARI (
   id INTEGER PRIMARY KEY,
   cari_ad varchar(255) not null,
   cari_tel varchar(32) not null,
   cari_adres varchar(255),
   cari_yetkili varchar(64) not null,
   cari_aktif INTEGER not null default 1;
);

create table if not exists URUN (
   id INTEGER PRIMARY KEY,
   urun_ad varchar(255) not null
   urun_aktif INTEGER not null default 1;
);

create table if not exists FIYAT (
   id INTEGER PRIMARY KEY,
   urun_id INTEGER not null,
   fiyat_cari real not null,
   fiyat_firin real,
   tarih date not null
);

create table if not exists SIPARIS (
   id INTEGER PRIMARY KEY,
   urun_id INTEGER not null,
   cari_id INTEGER not null,
   adet integer not null,
   tarih date not null
);

create table if not exists ODEME (
   id INTEGER PRIMARY KEY,
   cari_id INTEGER not null,
   miktar integer not null,
   tarih date not null
);

