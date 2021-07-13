var main = {
    init : function () {
        var _this = this;
        $('#btn-search').on('click', function () {
            _this.search();
        });

    },
    search : function () {
        var date = $('#date').val();

        $.ajax({
            type: 'POST',
            url: '/api/news/search/' + date,
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            // data: JSON.stringify(data)
        }).done(function() {
            alert('뉴스가 조회되었습니다.');
            window.location.href = '/news/'+date;
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

};
main.init();