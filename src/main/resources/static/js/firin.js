/*
let btnUrunIptal = document.getElementById("urunIptal");
let txtUrunAd = document.getElementById("urunAd");
btnUrunIptal.addEventListener("click", (e) => {
    //console.log(this, e);
    console.log(txtUrunAd);
    txtUrunAd.value = "";
    txtUrunAd.textContent = "";
    txtUrunAd.innerText = "";
    txtUrunAd.innerHTML = "";
    document.forms["urunForm"].reset();
});
 */

/*
<a href='javascript:updateCalisanGo("calisan", "76", document.action_form76,1);' title="Güncelle"><span class="fa fa-sync fa-lg text-warning"></span></a>
<a href='javascript:deleteCalisanGo("calisan", "76", document.action_form76,3);' title="Sil"><span class="fa fa-minus-circle fa-lg text-danger"></span></a>
 */


function silmeOnayOld(url, id) {
    var f_url = url + "/" + id;
    var f_onayid = id;
    var alert_mesaj = "Kaydı SİLMEK istediğinden EMİN misin?";
    if (confirm(alert_mesaj)) {
        let data = {"id": f_onayid};
        $.ajax({
            url: f_url,
            type: "GET",
            data: f_onayid,
            contentType: "text/plain",
            dataType: "text",
            crossOrigin: true,
            crossDomain: true,
        }).done(function (msg) {
            console.log(msg + " :> OKAY. " + f_onayid +  " id'li kayıt başarıyla silindi. URL: " + f_url);
            if (msg === 'OK') {
                $("#tr_" + id).css("display", "none");
            }
        }).fail(function (xhr, status, error) {
            alert("HATA oluştu! " + "STATUS: " + status + ", ERROR: " + error);
        });
    }
}

function silmeOnay() {
    var alert_mesaj = "Kaydı SİLMEK istediğinden EMİN misin?";
    if (confirm(alert_mesaj)) {
        return true;
    }
    return false;
}

function deleteSiparis(url, id, action_form, islem) {
    var f_calisanid = id;
    var f_calisanad = $.trim(action_form.liste_calisanad.value);
    var f_calisansoyad = $.trim(action_form.liste_calisansoyad.value);
    var f_islemid = islem;
    var alert_mesaj = f_calisanad + " " + f_calisansoyad;
    if (confirm(alert_mesaj + "\n\n" + "Siparis bilgisini SİLMEK istediğinden" + "\n\n" + "Emin misin?")) {
        $.ajax({
            url: url,
            type: "POST",
            data: {
                id: f_calisanid,
                islemid: f_islemid
            },
            dataType: "html",
            beforeSend: function (xhr) {
            },
            success: function (data, textStatus, xhr) {
            },
            error: function (xhr, textStatus, errorThrown) {
                alert("Hata Oluştu: " + textStatus + " , " + errorThrown);
            }
        }).done(function (msg) {
            if (is_number(msg)) {
                $("#tr" + id).css("display", "none");
                alert(alert_mesaj + " Çalışan bilgisi başarıyla SİLİNDİ");
            } else {
                alert("Hata: " + msg);
            }
        });
    }
}