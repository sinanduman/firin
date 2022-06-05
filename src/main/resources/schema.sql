create table if not exists CARI (
   id INTEGER PRIMARY KEY,
   cari_ad varchar(255) not null,
   cari_tel varchar(32) not null,
   cari_adres varchar(255),
   cari_yetkili varchar(64) not null,
   cari_aktif INTEGER not null default 1,
   tarih datetime not null,
);

create table if not exists URUN (
   id INTEGER PRIMARY KEY,
   urun_ad varchar(255) not null,
   urun_fiyat real not null default 1,
   aktif INTEGER not null default 1,
   tarih datetime not null
);

create table if not exists URUN_YEDEK (
   id INTEGER not null,
   urun_ad varchar(255) not null,
   urun_fiyat real not null default 1,
   aktif INTEGER not null default 1,
   tarih datetime not null
);

create table if not exists SIPARIS (
   id INTEGER PRIMARY KEY,
   cari_id INTEGER not null,
   urun_id INTEGER not null,
   adet integer not null,
   tutar real not null,
   onay integer not null,
   satis_iade integer not null default 1,
   tarih datetime not null
);

create table if not exists TAHSILAT (
   id INTEGER PRIMARY KEY,
   cari_id INTEGER not null,
   tutar integer not null,
   tarih_odeme varchar(32) not null,
   tarih datetime not null
);

