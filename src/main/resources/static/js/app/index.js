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
            type: 'GET',
        }).done(function() {
            window.location.href = '/news/'+date;
        }).fail(function (error) {
            alert('해당 날짜의 뉴스는 존재하지 않습니다.');
            alert(JSON.stringify(error));
        });
    }
};
main.init();